package com.example.Client;

import android.util.Log;

import java.util.Collection;

import Common.GameObject;
import Common.GameState;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 매치 진행 중 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateInGame implements GameState {
    private GameStateMatch _match;

    MatchStateInGame(GameStateMatch gameStateMatch) {
        _match = gameStateMatch;
        Core.getInstance().getUIManager().setTopText("게임이 시작되었습니다");
    }

    @Override
    public void update(long ms) {
        // TODO
    }

    @Override
    public void render(Renderer renderer, long ms) {
        Collection<GameObject> gameObjects = _match.getGameObjects();
        for (GameObject go : gameObjects){
            go.render(renderer);
        }
    }
}
