package Host;

import java.util.ArrayList;
import java.util.Random;

import Common.GameState;
import Common.MatchStateType;
import Common.OutputBitStream;
import Common.Player;
import Common.ReadOnlyList;
import Common.Util;

class MatchStateGetReadyHost implements GameState {
    private GameStateMatchHost _match;
    private int _countMS;
    private int _prevCount;
    ArrayList<Integer> _zombiePlayerIds = new ArrayList<>();
    private boolean _sentZombies = false;
    private int _numZombies;

    public MatchStateGetReadyHost(GameStateMatchHost gameStateMatchHost, int getReadyCount) {
        _match = gameStateMatchHost;
        _countMS = getReadyCount;
        _prevCount = _countMS / 1000;
    }

    @Override
    public void start() {
        chooseZombies();
    }

    private void chooseZombies() {
        Random rand = new Random();
        ReadOnlyList<Player> players = CoreHost.get().getMatch().getPlayers();
        _numZombies = Math.max(1, players.size() / 5);
        for (int i = 0; i < _numZombies; i++){
            int zombieIndex;
            int zombiePlayerId;
            do {
                zombieIndex = rand.nextInt(players.size());
                zombiePlayerId = players.get(zombieIndex).getProperty().getPlayerId();
            } while (_zombiePlayerIds.contains(zombiePlayerId));
            _zombiePlayerIds.add(zombiePlayerId);
        }
    }

    @Override
    public void update(long ms) {
        boolean countChanged = false;
        boolean startMatch = false;

        _countMS -= ms;
        if (_countMS / 1000 < _prevCount){
            _prevCount = _countMS / 1000;
            countChanged = true;
        }

        if (_countMS < 0)
            startMatch = true;

        OutputBitStream packet = CoreHost.get().getNetworkManager().getPacketToSend();

        Util.sendHas(packet, !_sentZombies);
        if (!_sentZombies){
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            packet.write(_numZombies, 8);
            for (int playerId : _zombiePlayerIds){
                packet.write(playerId, 8);
                Player victim = Util.findPlayerById(_match, playerId);
                _match.getCharacterFactory().setCharacterProperty(victim, 1);
            }
            _sentZombies = true;
        }

        Util.sendHas(packet, countChanged);
        if (countChanged){
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            packet.write(_prevCount, 8);
        }

        Util.sendHas(packet, startMatch);
        if (startMatch){
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.INGAME);
        }
    }
}
