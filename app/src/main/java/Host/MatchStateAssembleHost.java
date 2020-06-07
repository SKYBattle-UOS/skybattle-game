package Host;

import com.example.Client.Core;

import java.util.Collection;

import Common.CollisionState;
import Common.GameObject;
import Common.GameState;
import Common.ImageType;
import Common.Player;
import Common.Util;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;

class MatchStateAssembleHost implements GameState {
    private GameStateMatchHost _match;
    private boolean[] _assembleInit;
    private Collection<ClientProxy> _clients;
    private boolean _shouldSendAllInit;
    private GameObject _assemblePoint;

    public MatchStateAssembleHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
        _clients = CoreHost.get().getNetworkManager().getClientProxies();
        _assembleInit = new boolean[_clients.size()];
    }

    @Override
    public void update(long ms) {
        if (!_shouldSendAllInit){
            updateAssembleInit();
            _shouldSendAllInit = checkIfEverybodyInit();
        }

        boolean assembled = hasEverybodyAssembled();

        NetworkManager netManager = CoreHost.get().getNetworkManager();
        OutputBitStream outPacket = netManager.getPacketToSend();

        Util.sendHas(outPacket, _shouldSendAllInit);
        if (_shouldSendAllInit){
            if (!_match.isWorldSetterActive()){
                netManager.shouldSendThisFrame();
                _match.setWorldSetterActive();
                _match.setBattleGroundLatLon(37.714617, 127.045170);
                _match.createPlayers();

                for (Player p : _match.getPlayers()){
                    if (p.getProperty().getPlayerId() == 0) {
                        _assemblePoint = p.getGameObject();
                        _assemblePoint.setLook(ImageType.CIRCLE_WITH_MARKER);
                        _assemblePoint.setRadius(100);
                        return;
                    }
                }
            }
        }

        Util.sendHas(outPacket, assembled);
        if (assembled){
            _assemblePoint.setLook(ImageType.MARKER);
            _assemblePoint.setRadius(2.5f);

            GameObject respawnPoint = _match.createGameObject(Util.ItemClassId, true);
            respawnPoint.setPosition(_assemblePoint.getPosition());
            respawnPoint.setName("부활지점");
            respawnPoint.setRadius(20);
            respawnPoint.setLook(ImageType.INVISIBLE);

            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.SELECT_CHARACTER);
        }
    }

    private boolean hasEverybodyAssembled() {
        if (_match.isWorldSetterActive()){
            Collection<CollisionState> collisions = _match.getCollider().getCollisions(_assemblePoint);
            if (CoreHost.get().getMatch().getPlayers().size() != 0)
                return collisions.size() + 1 == CoreHost.get().getMatch().getPlayers().size();
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
        int i = -1;
        for (ClientProxy client : _clients){
            i++;
            InputBitStream packet = client.getPacketQueue().poll();
            if (packet == null || _assembleInit[i])
                continue;

            _assembleInit[i] = Util.hasMessage(packet);
        }
    }
}
