package Host;

import java.util.Collection;

import Common.GameObject;
import Common.GameState;
import Common.Util;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;

class MatchStateAssembleHost implements GameState {
    private GameStateMatchHost _match;
    private int _numPlayers;
    private boolean[] _assembleInit;
    private Collection<ClientProxy> _clients;
    private boolean _shouldSendAllInit;

    public MatchStateAssembleHost(GameStateMatchHost gameStateMatchHost, int numPlayers) {
        _match = gameStateMatchHost;
        _numPlayers = numPlayers;
        _assembleInit = new boolean[_numPlayers];
        _clients = CoreHost.getInstance().getNetworkManager().getClientProxies();
    }

    @Override
    public void update(long ms) {
        int i = 0;
        for (ClientProxy client : _clients){
            InputBitStream packet = client.getPacketQueue().poll();
            if (packet == null)
                continue;

            _assembleInit[i] = Util.hasMessage(packet);
            i++;
        }

        if (!_shouldSendAllInit) {
            _shouldSendAllInit = true;
            for (boolean b : _assembleInit)
                if (!b) {
                    _shouldSendAllInit = false;
                    break;
                }
        }

        Collection<GameObject> gos = _match.getGameObjects();
        boolean assembled = false;
        for (GameObject go : gos){
            // TODO
            // check if assembled
            // if otherwise return
            double[] pos = go.getPosition();
            if (pos[0] > 20)
                assembled = true;
                break;
        }

        OutputBitStream outPacket = CoreHost.getInstance().getNetworkManager().getPacketToSend();

        Util.sendHas(outPacket, _shouldSendAllInit);
        if (_shouldSendAllInit){
            CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();
            _shouldSendAllInit = true;

            if (!_match.isWorldSetterActive()){
                _match.createTempPlayers();
                _match.setWorldSetterActive();
            }
        }

        Util.sendHas(outPacket, assembled);
        if (assembled){
            CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.SELECT_CHARACTER);
        }
    }
}
