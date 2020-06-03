package com.example.Client;

import Common.GameObject;
import Common.GameState;
import Common.ReadOnlyList;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 매치 진행 중 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateInGame implements GameState {
    private GameStateMatch _match;
    private boolean _waiting = true;

    MatchStateInGame(GameStateMatch gameStateMatch) {
        _match = gameStateMatch;
    }

    @Override
    public void start() {
        Core.get().getUIManager().setDefaultTopText("게임이 시작되었습니다");
        Core.get().getUIManager().switchScreen(ScreenType.INGAME, () -> _waiting = false);
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;
    }


    @Override
    public void render(Renderer renderer, long ms) {
        ReadOnlyList<GameObject> gameObjects = _match.getWorld();
        for (GameObject go : gameObjects){
            ((Renderable) go).render(renderer);
        }
    }
}