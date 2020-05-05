package Host;

import com.example.myapplication.GameState;
import com.example.myapplication.MatchStateType;

public class GameStateMatchHost implements GameState {
    private GameState _currentState;
    // TODO
    private int _numPlayers;
    private int GETREADYCOUNT;

    public GameStateMatchHost(){
        _numPlayers = 2;
        GETREADYCOUNT = 10000;

        // TODO
        switchState(MatchStateType.ASSEMBLE);
    }

    @Override
    public void update(long ms) {

    }

    public void switchState(MatchStateType matchState) {
        switch (matchState) {
            case ASSEMBLE:
                _currentState = new MatchStateAssembleHost(this, _numPlayers);
                break;
            case SELECT_CHARACTER:
                _currentState = new MatchStateSelectCharacterHost(this);
                break;
            case GET_READY:
                _currentState = new MatchStateGetReadyHost(this, GETREADYCOUNT);
                break;
            case INGAME:
                _currentState = new MatchStateInGameHost(this);
                break;
        }
        _currentState.start();
    }
}
