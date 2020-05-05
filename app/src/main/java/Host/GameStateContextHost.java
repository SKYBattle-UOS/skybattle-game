package Host;

import Common.GameState;
import Common.GameStateType;

public class GameStateContextHost {
    private GameState _currentState;

    public void update(long ms){
        _currentState.update(ms);
    }

    public void switchState(GameStateType gameState){
        switch (gameState){
            case ROOM:
                _currentState = new GameStateRoomHost(this);
                break;
            case MATCH:
                _currentState = new GameStateMatchHost();
                break;
        }
        _currentState.start();
    }
}
