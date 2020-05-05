package com.example.Client;

import android.content.Context;
import android.os.SystemClock;

import Common.GameStateType;

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
    private InputManager _inputManager;
    private Renderer _renderer;
    private GameStateContext _stateContext;
    private InstructionManager _instructionManager;
    private GameObjectFactory _gameObjectFactory;
    private UIManager _uiManager;

    private Core(Context context){
        _isInitialized = false;
        _context = context;
        _inputManager = new InputManager();
        _renderer = new Renderer();
        _stateContext = new GameStateContext();
        _instructionManager = new ReplayInstructionManager(context);
        _gameObjectFactory = new GameObjectFactory();
        _uiManager = new UIManager();

        registerGameObjects();
    }

    public void init(){
        if (!_isInitialized){
            _stateContext.switchState(GameStateType.MAIN);
            _isInitialized = true;
        }
    }

    public static void createInstance(Context context){
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
    public void run(){
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
    public void run(long ms){
        _instructionManager.update(ms);

        _stateContext.update(ms);
        _stateContext.render(_renderer, ms);

        _renderer.render(ms);

        _inputManager.update(ms);
    }
    // endregion

    public InstructionManager getInstructionManager(){
        return _instructionManager;
    }

    public GameObjectFactory getGameObjectFactory(){
        return _gameObjectFactory;
    }

    public InputManager getInputManager() { return _inputManager; }

    public UIManager getUIManager() { return _uiManager; }

    private void registerGameObjects(){
        // WARNING: should be listed in the same order as that in the server
        TempPlayer.classId = _gameObjectFactory.registerCreateMethod(TempPlayer::createInstance);
    }
}
