package Host;

import Common.GameState;

class MatchStateInGameHost implements GameState {
    private GameStateMatchHost _match;

    public MatchStateInGameHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
    }

    @Override
    public void update(long ms) {
        // TODO
    }
}
