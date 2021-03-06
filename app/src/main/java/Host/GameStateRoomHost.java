package Host;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;
import Common.RoomSettings;
import Common.RoomUserInfo;
import Common.Util;

public class GameStateRoomHost implements GameState, ConnectionListener {
    private GameStateContextHost _parent;
    private RoomSettings _settingsToSend = new RoomSettings();
    private int _tempNameNumber = 1;
    private boolean _usersDirty;

    public GameStateRoomHost(GameStateContextHost parent){
        _parent = parent;
    }

    @Override
    public void start() {
        CoreHost.get().getNetworkManager().setConnectionListener(this);
    }

    @Override
    public void finish() {
        CoreHost.get().getNetworkManager().removeConnectionListener(this);
    }

    @Override
    public void update(long ms) {
        receive();
        send();
    }

    private void receive(){
        Collection<ClientProxy> clients = CoreHost.get().getNetworkManager().getClientProxies();
        for (ClientProxy client : clients){
            InputBitStream packet = client.getRawPacketQueue().poll();
            if (packet == null)
                continue;

            if (client == CoreHost.get().getNetworkManager().getHostClientProxy()){
                _settingsToSend.readFromStream(packet);

                if (_settingsToSend.startButtonPressed)
                    onStartButtonPressed();
            }

            if (packet.read(1) == 1){
                _parent.getUsers().get(client).readFromStream(packet);
                _usersDirty = true;
            }
        }
    }

    private void send() {
        OutputBitStream packetToSend = CoreHost.get().getNetworkManager().getPacketToSend();
        if (_settingsToSend.writeToStream(packetToSend))
            CoreHost.get().getNetworkManager().shouldSendThisFrame();

        Util.sendHas(packetToSend, _usersDirty);
        if (_usersDirty){
            packetToSend.write(_parent.getUsers().size(), 8);
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            for (Map.Entry<ClientProxy, RoomUserInfo> e : _parent.getUsers().entrySet()){
                e.getValue().writeToStream(packetToSend);
            }
            _usersDirty = false;
        }

        _settingsToSend = new RoomSettings();
    }

    private void onStartButtonPressed() {
        _parent.switchState(GameStateType.MATCH);
    }

    @Override
    public void onNewConnection(ClientProxy client){
        RoomUserInfo info = new RoomUserInfo();
        info.name = String.format("익명%d", _tempNameNumber++);
        info.team = 0;
        info.playerId = client.getPlayerId();
        _parent.getUsers().put(client, info);
        _usersDirty = true;
        _settingsToSend.roomTitleChanged = true;
    }

    @Override
    public void onConnectionLost(ClientProxy client){
        _parent.getUsers().remove(client);
        _usersDirty = true;
    }
}
