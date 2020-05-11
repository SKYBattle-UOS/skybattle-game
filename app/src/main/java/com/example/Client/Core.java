package com.example.Client;

import android.os.SystemClock;

import Common.GameStateType;
import Common.TempPlayer;

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

    private boolean _isInitialized;
    private Renderer _renderer;
    private GameStateContext _stateContext;
    private PacketManager _packetManager;
    private GameObjectFactory _gameObjectFactory;
    private UIManager _uiManager;

    private Core(){
        _isInitialized = false;
        _stateContext = new GameStateContext();
        _packetManager = new ReplayPacketManager();
        _gameObjectFactory = new GameObjectFactory();
        _uiManager = new UIManager();

        registerGameObjects();
    }

    private void init(){
        if (!_isInitialized){
            _stateContext.switchState(GameStateType.MAIN);
            _isInitialized = true;
        }
    }

    public static Core getInstance(){
        if (_coreInstance == null){
            _coreInstance = new Core();
            _coreInstance.init();
            (new Thread(() -> _coreInstance.run())).start();
        }
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
        _stateContext.update(ms);
        _packetManager.update();

        _stateContext.render(_renderer, ms);

        if (_renderer != null)
            _renderer.render(ms);

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

    private void registerGameObjects(){
        // WARNING: should be listed in the same order as that in the server
        TempPlayer.classId = _gameObjectFactory.registerCreateMethod(TempPlayer::createInstance);
    }
}
