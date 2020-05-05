package Host;

import com.example.myapplication.GameState;

class MatchStateInGameHost implements GameState {
    private GameStateMatchHost _gameStateMatchHost;

    public MatchStateInGameHost(GameStateMatchHost gameStateMatchHost) {
        _gameStateMatchHost = gameStateMatchHost;
    }

    @Override
    public void update(long ms) {
        // TODO
    }
}
