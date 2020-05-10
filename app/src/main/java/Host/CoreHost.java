package Host;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Collection;

import Common.GameObject;
import Common.GameStateType;

public class CoreHost {
    private static CoreHost _instance;
    private static final int _port = 9998;

    private boolean _isInitialized;
    private NetworkManager _networkManager;
    private GameStateContextHost _gameStateContext;
    private ArrayList<GameObject> _gameObjects;

    private CoreHost(){
        _isInitialized = false;
        _networkManager = new NetworkManager(_port);
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

    private void run(){
        long prev = SystemClock.uptimeMillis();
        long ms;

        while (true){
            long now = SystemClock.uptimeMillis();
            ms = now - prev;
            run(ms);

            long elapsed = SystemClock.uptimeMillis() - now;
            if (elapsed < 33)
                try {
                    Thread.sleep(33 - elapsed);
                } catch (InterruptedException e) {
                    // nothing
                }

            prev = now;
        }
    }

    private void run(long ms){
        _networkManager.update(ms);
    }

    public void destroy(){
        // TODO
    }

    public NetworkManager getNetworkManager(){
        return _networkManager;
    }

    public Collection<GameObject> getGameObjects(){
        return _gameObjects;
    }
}
