package Host;

import java.util.HashMap;
import java.util.Map;

import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;
import Common.Player;
import Common.Util;

class MatchStateSelectCharacterHost implements GameState, ConnectionListener {
    private GameStateMatchHost _match;
    private HashMap<Integer, Integer> _characters = new HashMap<>();

    public MatchStateSelectCharacterHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
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

    private void send() {
        OutputBitStream outputPacket = CoreHost.get().getNetworkManager().getPacketToSend();
        int numClients = CoreHost.get().getNetworkManager().getClientProxies().size();
        boolean allset = _characters.size() == numClients;
        Util.sendHas(outputPacket, allset);
        if (allset) {
            createCharacters();
            outputPacket.write(_characters.size(), 8);
            for (Map.Entry<Integer, Integer> e : _characters.entrySet()){
                outputPacket.write(e.getKey(), 32);
                outputPacket.write(e.getValue(), 8);
            }
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.GET_READY);
        }
    }

    private void createCharacters() {
        for (Map.Entry<Integer, Integer> e : _characters.entrySet()){
            Player player = Util.findPlayerById(_match, e.getKey());
            _match.getCharacterFactory().setCharacterProperty(player, e.getValue());
        }
    }

    private void receive() {
        for (ClientProxy client : CoreHost.get().getNetworkManager().getClientProxies()){
            InputBitStream packet = client.getPacketQueue().poll();
            if (packet == null)
                continue;

            if (Util.hasMessage(packet)){
                _characters.put(client.getPlayerId(), packet.read(8));
            }
        }
    }

    @Override
    public void onConnectionLost(ClientProxy client) {
        _characters.remove(client.getPlayerId());
    }

    @Override
    public void onNewConnection(ClientProxy client) {

    }
}
