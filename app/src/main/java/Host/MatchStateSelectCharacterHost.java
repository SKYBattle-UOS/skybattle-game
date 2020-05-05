package Host;

import com.example.myapplication.GameState;

class MatchStateSelectCharacterHost implements GameState {
    private GameStateMatchHost _match;

    public MatchStateSelectCharacterHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
    }

    @Override
    public void update(long ms) {
        // TODO
    }
}
