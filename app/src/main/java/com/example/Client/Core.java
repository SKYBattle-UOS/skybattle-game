package com.example.Client;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;

import Common.Camera;
import Common.GameStateType;
import Common.LatLonByteConverter;
import Common.Match;
import Common.Util;
import Host.CoreHost;

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

    private Core(Activity activity, Context context){
        _appContext = context;
        _isInitialized = false;
        _packetManager = new NetworkPacketManager();
        _gameObjectFactory = new GameObjectFactory();
        _uiManager = new AndroidUIManager();
        _converter = new LatLonByteConverter();
        _inputManager = new InputManager( activity,context, _converter);
        _stateContext = new GameStateContext(_converter);

        Util.registerGameObjects(_gameObjectFactory);
    }

    private void init(){
        // TODO
        if (!_isInitialized){
            CoreHost.getInstance().getNetworkManager().open();
            _stateContext.switchState(GameStateType.MAIN);
            _isInitialized = true;
            ((NetworkPacketManager) _packetManager).init("localhost");
        }
    }

    public static void createInstance(Activity activity, Context context){
        if (_coreInstance == null){
            _coreInstance = new Core(activity, context);
            _coreInstance.init();
            (new Thread(() -> _coreInstance.run())).start();
        }
    }

    public static Core getInstance(){
        return _coreInstance;
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
}
