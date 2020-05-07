package com.example.Client;

import Common.GameState;
import Common.GameStateType;

/**
 * 앱의 각 화면에 대한 상태패턴의 상태 객체 중 메인화면
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 * @see GameStateContext
 */
public class GameStateMain implements GameState {
    static final int switchScreenPort = 0;

    private GameStateContext _parent;

    GameStateMain(GameStateContext parent){
        _parent = parent;
    }

    @Override
    public void start() {
        Core.getInstance().getUIManager().registerCallback(switchScreenPort, () -> {
            _parent.switchState(GameStateType.ROOM);
            Core.getInstance().getUIManager().switchScreen(ScreenType.ROOM);
        } );
    }

    @Override
    public void update(long ms) {
        // TODO
    }

    @Override
    public void render(Renderer renderer, long ms) {
//        Log.i("Stub", "GameStateMain: Showing Main Screen UI");
    }
}