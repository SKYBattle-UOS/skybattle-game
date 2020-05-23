package Host;

import com.example.Client.Core;

import java.util.Collection;

import Common.CollisionState;
import Common.GameObject;
import Common.GameState;
import Common.PlayerHost;
import Common.Util;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;

class MatchStateAssembleHost implements GameState {
    private GameStateMatchHost _match;
    private boolean[] _assembleInit;
    private Collection<ClientProxy> _clients;
    private boolean _shouldSendAllInit;

    // TODO
    private GameObject _assemblePoint;

    public MatchStateAssembleHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
        _clients = CoreHost.getInstance().getNetworkManager().getClientProxies();
        _assembleInit = new boolean[_clients.size()];
    }

    @Override
    public void update(long ms) {
        updateAssembleInit();

        if (!_shouldSendAllInit)
            _shouldSendAllInit = checkIfEverybodyInit();

        boolean assembled = hasEverybodyAssembled();

        OutputBitStream outPacket = CoreHost.getInstance().getNetworkManager().getPacketToSend();

        Util.sendHas(outPacket, _shouldSendAllInit);
        if (_shouldSendAllInit){
            CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();

            if (!_match.isWorldSetterActive()){
                _match.setWorldSetterActive();
                _match.setBattleGroundLatLon(37.714617, 127.045170);
                _match.createPlayers();

                for (GameObject go : _match.getWorld()){
                    if (go.getName() == "여기여기 모여라") {
                        _assemblePoint = go;
                        return;
                    }
                }
            }
        }

        Util.sendHas(outPacket, assembled);
        if (assembled){
            _assemblePoint.scheduleDeath();

            CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.SELECT_CHARACTER);
        }
    }

    private boolean hasEverybodyAssembled() {
        if (_match.isWorldSetterActive()){
            Collection<CollisionState> collisions = _match.getCollider().getCollisions(_assemblePoint);
            if (Core.getInstance().getMatch().getPlayers().size() != 0)
                return collisions.size() == Core.getInstance().getMatch().getPlayers().size();
        }
        return false;
    }

    private boolean checkIfEverybodyInit() {
        for (boolean b : _assembleInit)
            if (!b) {
                return false;
            }
        return true;
    }

    private void updateAssembleInit() {
        int i = 0;
        for (ClientProxy client : _clients){
            InputBitStream packet = client.getPacketQueue().poll();
            if (packet == null)
                continue;

            _assembleInit[i] = Util.hasMessage(packet);
            i++;
        }
    }
}
