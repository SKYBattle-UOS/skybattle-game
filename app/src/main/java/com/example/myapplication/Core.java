package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

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

    private Context _context;
    private InputManager _inputManager;
    private Renderer _renderer;
    private GameStateContext _stateContext;
    private InstructionManager _instructionManager;
    private GameObjectFactory _gameObjectFactory;

    private Core(Context context){
        this._context = context;
        _inputManager = new InputManager();
        _renderer = new Renderer();
        _stateContext = new GameStateContext();
        _instructionManager = new ReplayInstructionManager(context);
        _gameObjectFactory = new GameObjectFactory();

        registerGameObjects();
    }

    public static void createInstance(Context context){
        _coreInstance = new Core(context);
    }

    public static Core getInstance(){
        return _coreInstance;
    }

    /**
     * 애플리케이션 로직의 시작점.
     */
    public void run(){

        // TODO: DEBUG DELETE
        // region DEBUG
        int ms = 1000; // update every 1 second (1000 millisecond)

        run(ms);
        run(ms);

        // room button pressed
        Log.i("Stub", "Core: Room Enter Button Pressed");
        _stateContext.switchState(GameStateType.ROOM);

        // continue running
        for (int i = 0; i < 50; i++){
            run(ms);
        }
        // endregion
    }

    // TODO: DEBUG DELETE
    // region DEBUG
    public void run(int ms){
        ((ReplayInstructionManager) _instructionManager).tempUpdate(ms);

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

    private void registerGameObjects(){
        // WARNING: should be listed in the same order as that in the server
        TempPlayer.classId = _gameObjectFactory.registerCreateMethod(TempPlayer::createInstance);
    }
}
