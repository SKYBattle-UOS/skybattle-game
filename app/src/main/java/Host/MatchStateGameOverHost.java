package Host;

import Common.GameState;

public class MatchStateGameOverHost implements GameState {
    private GameStateMatchHost _match;

    public MatchStateGameOverHost(GameStateMatchHost match){
        _match = match;
    }

    @Override
    public void start() {

    }
}
