package com.example.Client;

import android.content.Context;
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
    private Context _context;
    private IOManager _IOManager;
    private Renderer _renderer;
    private GameStateContext _stateContext;
    private PacketManager _packetManager;
    private GameObjectFactory _gameObjectFactory;
    private UIManager _uiManager;

    private Core(Context context){
        _isInitialized = false;
        _context = context;
        _stateContext = new GameStateContext();
        _packetManager = new ReplayPacketManager();
        _IOManager = new IOManager(_packetManager);
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

    static void createInstance(Context context){
        if (_coreInstance == null){
            _coreInstance = new Core(context);
            _coreInstance.init();
            (new Thread(() -> _coreInstance.run())).start();
        }
    }

    public static Core getInstance(){
        return _coreInstance;
    }

    /**
     * 애플리케이션 로직의 시작점.
     */
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

    // TODO: DEBUG DELETE
    // region DEBUG
    private void run(long ms){
        _packetManager.update(ms);

        _stateContext.update(ms);
        _stateContext.render(_renderer, ms);

        if (_renderer != null)
            _renderer.render(ms);

        _IOManager.update(ms);
    }
    // endregion

    public PacketManager getPakcetManager(){
        return _packetManager;
    }

    public GameObjectFactory getGameObjectFactory(){
        return _gameObjectFactory;
    }

    public IOManager getIOManager() { return _IOManager; }

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
