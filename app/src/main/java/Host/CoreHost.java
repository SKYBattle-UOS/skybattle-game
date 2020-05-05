package Host;

import com.example.myapplication.GameStateType;
import com.example.myapplication.InputBitStream;

public class CoreHost {
    private static CoreHost _instance;

    private boolean _isInitialized;
    private NetworkManager _networkManager;
    private GameStateContextHost _gameStateContext;

    private CoreHost(){
        _isInitialized = false;
        _networkManager = new NetworkManager();
        _gameStateContext = new GameStateContextHost();
    }

    public static CoreHost getInstance(){
        if (_instance == null){
            _instance = new CoreHost();
            _instance.init();
        }

        return _instance;
    }

    public void init(){
        if (!_isInitialized){
            _gameStateContext.switchState(GameStateType.ROOM);
            _isInitialized = true;
        }
    }

    public NetworkManager getNetworkManager(){
        return _networkManager;
    }
}
