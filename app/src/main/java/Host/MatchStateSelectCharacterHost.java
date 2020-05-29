package Host;

import java.util.Collection;

import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;
import Common.Util;

class MatchStateSelectCharacterHost implements GameState {
    private GameStateMatchHost _match;
    private boolean[] _characterSelected;
    private Collection<ClientProxy> _clients;

    // TODO
    private long elapsed;

    public MatchStateSelectCharacterHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
        _clients = CoreHost.get().getNetworkManager().getClientProxies();
        _characterSelected = new boolean[_clients.size()];
    }

    @Override
    public void update(long ms) {
        int i = 0;
        for (ClientProxy client : _clients){
            InputBitStream packet = client.getPacketQueue().poll();
            if (packet == null)
                continue;

            _characterSelected[i] = Util.hasMessage(packet);
            i++;
        }

        boolean allset = true;
        for (boolean b : _characterSelected) {
            if (!b) {
                allset = false;
                break;
            }
        }

        elapsed += ms;
        if (elapsed > 1000)
            allset = true;

        OutputBitStream outputPacket = CoreHost.get().getNetworkManager().getPacketToSend();
        Util.sendHas(outputPacket, allset);
        if (allset) {
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.GET_READY);
        }
    }
}
