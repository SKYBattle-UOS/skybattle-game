package com.example.Client;

import android.util.Log;

import java.util.Collection;
import java.util.Locale;

import Common.GameObject;
import Common.GameState;
import Common.MatchStateType;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 준비화면.
 * 게임시작 전 러너들이 도망가는 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21a
 */
public class MatchStateGetReady implements GameState {
    private final String TOP_TEXT = "도망가세요. 남은시간 : %d초";
    private int _countDown;
    private int _prevCount;
    private GameStateMatch _match;

    MatchStateGetReady(GameStateMatch parent, int countInMS){
        _match = parent;
        _countDown = countInMS;
        _prevCount = _countDown / 1000;
        Core.getInstance().getUIManager().setText(String.format(Locale.getDefault(), TOP_TEXT, _prevCount));
    }

    @Override
    public void update(long ms) {
        _countDown -= ms;
        if (_countDown < 0){
            _match.switchState(MatchStateType.INGAME);
            Core.getInstance().getUIManager().switchScreen(ScreenType.INGAME);
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        if (_prevCount != _countDown / 1000){
            _prevCount = _countDown / 1000;
            Core.getInstance().getUIManager().setText(String.format(Locale.getDefault(), TOP_TEXT, _countDown / 1000));
        }

        Collection<GameObject> gameObjects = _match.getGameObjects();
        for (GameObject go : gameObjects){
            go.render(renderer);
        }
    }
}
