package Host;

import java.io.IOException;
import java.util.Collection;

import Common.GameState;
import Common.InputBitStream;
import Common.OutputBitStream;

class MatchStateSelectCharacterHost implements GameState {
    private GameStateMatchHost _match;
    private boolean[] _characterSelected;
    private Collection<ClientProxy> _clients;

    public MatchStateSelectCharacterHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
        _clients = CoreHost.getInstance().getNetworkManager().getClientProxies();
        _characterSelected = new boolean[_clients.size()];
    }

    @Override
    public void update(long ms) {
        int i = 0;
        for (ClientProxy client : _clients){
            InputBitStream packet = client.getPacketQueue().poll();
            if (packet == null)
                continue;

            _characterSelected[i] = hasSelectedCharacter(packet);
            i++;
        }

        for (boolean b : _characterSelected)
            if (!b) return;

        OutputBitStream outputPacket = CoreHost.getInstance().getNetworkManager().getPacketToSend();
        sendHasCustomMessage(outputPacket);
        sendEverybodySelectedCharacter(outputPacket);
    }

    private void sendEverybodySelectedCharacter(OutputBitStream outputPacket) {
        try {
            outputPacket.write(1, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasSelectedCharacter(InputBitStream packet) {
        return packet.read(1) == 1;
    }

    private void sendHasCustomMessage(OutputBitStream outPacket) {
        try {
            outPacket.write(1, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
