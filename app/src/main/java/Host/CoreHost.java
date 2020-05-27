package Host;

import android.os.SystemClock;

import Common.AndroidTime;
import Common.GameStateType;
import Common.Match;
import Common.Time;

public class CoreHost {
    private static CoreHost _instance;

    private boolean _isInitialized;
    private NetworkManager _networkManager;
    private GameStateContextHost _gameStateContext;
    private Match _match;
    private Time _time;

    private CoreHost(){
        _isInitialized = false;
        _networkManager = new NetworkManager();
        _gameStateContext = new GameStateContextHost();
        _time = new AndroidTime();
    }

    public static CoreHost getInstance(){
        if (_instance == null){
            _instance = new CoreHost();
            _instance.init();
            (new Thread(()->_instance.run())).start();
        }

        return _instance;
    }

    private void init(){
        if (!_isInitialized){
            _gameStateContext.switchState(GameStateType.ROOM);
            _isInitialized = true;
        }
    }

    private void run(){
        while (true){
            _time.setStartOfFrame();
            run(_time.getFrameInterval());

            int elapsed = _time.getElapsedSinceStart();
            if (elapsed < 33)
                try {
                    Thread.sleep(33 - elapsed);
                } catch (InterruptedException e) {
                    // nothing
                }
        }
    }

    private void run(long ms){
        _gameStateContext.update(ms);
        _networkManager.update();
    }

    public void destroy(){
        // TODO
    }

    public NetworkManager getNetworkManager(){
        return _networkManager;
    }

    public void setMatch(Match match) {
        _match = match;
    }
    
    public Match getMatch(){
        return _match;
    }

    public Time getTime(){ return _time; }
}
