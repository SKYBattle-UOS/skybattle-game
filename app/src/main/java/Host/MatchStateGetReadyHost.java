package Host;

import com.example.myapplication.GameState;

class MatchStateGetReadyHost implements GameState {
    private GameStateMatchHost _match;
    private int _getReadyCount;

    public MatchStateGetReadyHost(GameStateMatchHost gameStateMatchHost, int getReadyCount) {
        _match = gameStateMatchHost;
        _getReadyCount = getReadyCount;
    }

    @Override
    public void update(long ms) {
        // TODO
    }
}
