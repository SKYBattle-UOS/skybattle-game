package Host;

import Common.GameState;
import Common.GameStateType;
import Common.LatLonByteConverter;

public class GameStateContextHost {
    private GameState _currentState;
    private LatLonByteConverter _converter = new LatLonByteConverter();

    public void update(long ms){
        _currentState.update(ms);
    }

    public void switchState(GameStateType gameState){
        switch (gameState){
            case ROOM:
                _currentState = new GameStateRoomHost(this);
                break;
            case MATCH:
                _currentState = new GameStateMatchHost(this);
                break;
        }
        _currentState.start();
    }

    public LatLonByteConverter getConverter(){
        return _converter;
    }
}
