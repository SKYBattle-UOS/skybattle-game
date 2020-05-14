package Host;

import java.io.IOException;
import java.util.Collection;

import Common.GameObject;
import Common.GameState;
import Common.InputBitStream;
import Common.OutputBitStream;

class MatchStateAssembleHost implements GameState {
    private GameStateMatchHost _match;
    private int _numPlayers;
    private boolean[] _assembleInit;
    private Collection<ClientProxy> _clients;
    private boolean _hasSentEverybodyInit;


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

            _assembleInit[i] = isAssembleInit(packet);
            i++;
        }

        OutputBitStream outPacket = CoreHost.getInstance().getNetworkManager().getPacketToSend();

        if (!_hasSentEverybodyInit){
            for (boolean b : _assembleInit)
                if (!b) return;

            CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();
            sendHasCustomMessage(outPacket);
            sendEverybodyInitialized(outPacket);
            _hasSentEverybodyInit = true;
            return;
        }

        Collection<GameObject> gos = _match.getGameObjects();
        for (GameObject go : gos){
            // TODO
            // check if assembled
            // if otherwise return
            return;
        }

        CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();
        sendHasCustomMessage(outPacket);
        sendAssembleComplete(outPacket);
    }

    private void sendAssembleComplete(OutputBitStream outPacket) {
        try {
            outPacket.write(1, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendHasCustomMessage(OutputBitStream outPacket) {
        try {
            outPacket.write(1, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEverybodyInitialized(OutputBitStream outPacket) {
        try {
            outPacket.write(1, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAssembleInit(InputBitStream packet){
        int received = packet.read(1);
        return received == 1;
    }
}
