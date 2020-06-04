package Host;

import Common.GameState;
import Common.GameStateType;
import Common.LatLonByteConverter;

public class GameStateContextHost {
    private GameState _currentState = new GameState() {
        @Override
        public void start() {
            // nothing
        }
    };
    private LatLonByteConverter _converter = new LatLonByteConverter();

    public void update(long ms){
        _currentState.update(ms);
    }

    public void switchState(GameStateType gameState){
        _currentState.finish();
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
