package Host;

import com.example.myapplication.GameState;

class MatchStateAssembleHost implements GameState {
    private GameStateMatchHost _match;
    private int _numPlayers;

    public MatchStateAssembleHost(GameStateMatchHost gameStateMatchHost, int numPlayers) {
        _match = gameStateMatchHost;
        _numPlayers = numPlayers;
    }

    @Override
    public void update(long ms) {
        // TODO
    }
}
