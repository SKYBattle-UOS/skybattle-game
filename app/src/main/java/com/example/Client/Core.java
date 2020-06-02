package com.example.Client;

import android.content.Context;

import Common.AndroidTime;
import Common.Camera;
import Common.GameStateType;
import Common.LatLonByteConverter;
import Common.Time;
import Common.Util;

/**
 * 앱이 사용하는 여러 클래스를 초기화하고 작동순서대로 호출합니다.
 * 게임루프가 들어있습니다.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class Core {

    private static Core _coreInstance;

    private Context _appContext;
    private boolean _isInitialized;
    private Renderer _renderer;
    private Camera _camera;
    private GameStateContext _stateContext;
    private PacketManager _packetManager;
    private GameObjectFactory _gameObjectFactory;
    private UIManager _uiManager;
    private InputManager _inputManager;
    private LatLonByteConverter _converter;
    private Match _match;
    private Time _time;
    private Thread _runThread;
    private boolean _isHost;

    private Core(Context context){
        _appContext = context;
        _isInitialized = false;
        _gameObjectFactory = new GameObjectFactory();
        _uiManager = new AndroidUIManager();
        _converter = new LatLonByteConverter();
        _inputManager = new InputManager(context, _converter);
        _stateContext = new GameStateContext(_converter);
        _time = new AndroidTime();

        Util.registerGameObjects(_gameObjectFactory);
    }

    public static void createInstance(Context context){
        if (_coreInstance == null){
            _coreInstance = new Core(context);

            if (!_coreInstance._isInitialized){
                _coreInstance._stateContext.switchState(GameStateType.MAIN);
                _coreInstance._isInitialized = true;
            }
        }
    }

    public static Core get(){
        return _coreInstance;
    }

    public void open(String host, boolean isHost){
        _packetManager = new NetworkPacketManager();
        ((NetworkPacketManager) _packetManager).init(host,
            b -> {
                if (b){
                    _runThread = new Thread(() -> _coreInstance.run());
                    _runThread.start();
                    ((GameStateMain) _stateContext.getState()).enterRoom();
                    _isHost = isHost;
                }
                else {
                    _isHost = false;
                    _packetManager = null;
                    _uiManager.failConnection();
                }
            }
        );
    }

    public void close(){
        ((GameStateRoom) _stateContext.getState()).exitRoom();
        ((NetworkPacketManager) _packetManager).close();
        _runThread.interrupt();
        _packetManager = null;
        _isHost = false;
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
                    // room exit
                    return;
                }
        }
    }

    private void run(long ms){
        _inputManager.update(ms);
        _stateContext.update(ms);
        _packetManager.update();

        _stateContext.render(_renderer, ms);

        if (_renderer != null)
            _renderer.render(ms);

        _uiManager.update(ms);
    }

    public PacketManager getPakcetManager(){
        return _packetManager;
    }

    public GameObjectFactory getGameObjectFactory(){
        return _gameObjectFactory;
    }

    public UIManager getUIManager() { return _uiManager; }

    public Renderer getRenderer() { return _renderer; }

    public void setRenderer(Renderer renderer){
        _renderer = renderer;
    }

    public Camera getCamera() { return _camera; }

    public void setCamera(Camera camera){
        _camera = camera;
    }

    public InputManager getInputManager() { return _inputManager; }

    public Match getMatch(){ return _match; }

    public void setMatch(Match match){ _match = match; }

    public Time getTime(){ return _time; }

    public boolean isHost(){ return _isHost; }
}
