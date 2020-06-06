package Host;

import com.example.Client.Core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Common.CharacterFactory;
import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;
import Common.Player;
import Common.Util;

class MatchStateSelectCharacterHost implements GameState, ConnectionListener {
    private GameStateMatchHost _match;
    private HashMap<Integer, Integer> _characters = new HashMap<>();
    private CharacterFactory _charFactory;

    public MatchStateSelectCharacterHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
        _charFactory = new CharacterFactory(CoreHost.get().getGameObjectFactory());
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
            try {
                outputPacket.write(_characters.size(), 8);
                for (Map.Entry<Integer, Integer> e : _characters.entrySet()){
                    outputPacket.write(e.getKey(), 32);
                    outputPacket.write(e.getValue(), 8);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.GET_READY);
        }
    }

    private void createCharacters() {
        for (Map.Entry<Integer, Integer> e : _characters.entrySet()){
            Player player = findPlayer(e.getKey());
            _charFactory.setCharacterProperty(player, e.getValue());
        }
    }

    private Player findPlayer(int playerId) {
        for (Player p : _match.getPlayers()){
            if (p.getProperty().getPlayerId() == playerId)
                return p;
        }

        return null;
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
