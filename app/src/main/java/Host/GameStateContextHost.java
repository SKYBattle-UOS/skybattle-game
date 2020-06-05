package Host;

import java.util.HashMap;

import Common.GameState;
import Common.GameStateType;
import Common.LatLonByteConverter;
import Common.RoomUserInfo;

public class GameStateContextHost {
    private GameState _currentState = new GameState() {
        @Override
        public void start() {
            // nothing
        }
    };
    private LatLonByteConverter _converter = new LatLonByteConverter();
    private HashMap<ClientProxy, RoomUserInfo> _users = new HashMap<>();


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

    public HashMap<ClientProxy, RoomUserInfo> getUsers(){
        return _users;
    }
}
