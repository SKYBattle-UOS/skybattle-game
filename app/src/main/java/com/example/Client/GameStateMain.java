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
    private GameStateContext _parent;
    private boolean _goodToGo;

    GameStateMain(GameStateContext parent){
        _parent = parent;
    }

    @Override
    public void update(long ms) {
        if (_goodToGo)
            _parent.switchState(GameStateType.ROOM);
    }

    public void enterRoom() {
        Core.get().getUIManager().switchScreen(ScreenType.ROOM, () -> _goodToGo = true);
    }
}