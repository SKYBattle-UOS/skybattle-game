package Host;

import Common.AndroidTime;
import Common.GameStateType;
import Common.Time;

public class CoreHost {
    private static CoreHost _instance;

    private NetworkManager _networkManager;
    private GameStateContextHost _gameStateContext;
    private MatchHost _match;
    private Time _time;

    private CoreHost(){
        _networkManager = new NetworkManager();
        _gameStateContext = new GameStateContextHost();
        _time = new AndroidTime();
    }

    public static void createInstance(){
        if (_instance != null) return;

        _instance = new CoreHost();
        _instance.init();
        (new Thread(()->_instance.run())).start();
    }

    public void destroyInstance(){
        // TODO
    }

    public static CoreHost get(){
        return _instance;
    }

    private void init(){
        _gameStateContext.switchState(GameStateType.ROOM);
        _networkManager.open();
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

    public NetworkManager getNetworkManager(){
        return _networkManager;
    }

    public void setMatch(MatchHost match) {
        _match = match;
    }
    
    public MatchHost getMatch(){
        return _match;
    }

    public Time getTime(){ return _time; }
}
