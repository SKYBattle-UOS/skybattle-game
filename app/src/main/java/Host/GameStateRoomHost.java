package Host;

import android.util.Log;

import java.io.IOException;
import java.util.Collection;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;
import Common.RoomSettings;

public class GameStateRoomHost implements GameState {
    private GameStateContextHost _parent;
    private RoomSettings _settingsToSend = new RoomSettings();

    public GameStateRoomHost(GameStateContextHost parent){
        _parent = parent;
    }

    @Override
    public void update(long ms) {
        receive();
        send();
    }

    private void send() {
        OutputBitStream packetToSend = CoreHost.get().getNetworkManager().getPacketToSend();
        if (_settingsToSend.writeToStream(packetToSend))
            CoreHost.get().getNetworkManager().shouldSendThisFrame();

        _settingsToSend = new RoomSettings();
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
        }
    }

    private void onStartButtonPressed() {
        _parent.switchState(GameStateType.MATCH);
    }
}
