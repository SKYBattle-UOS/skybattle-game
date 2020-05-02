package com.example.myapplication;

import android.util.Log;

/**
 * 앱의 각 화면에 대한 상태패턴의 상태 객체 중 메인화면
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 * @see GameStateContext
 */
public class GameStateMain implements GameState {
    @Override
    public void update(int ms) {
        // TODO
    }

    @Override
    public void render(Renderer renderer, int ms) {
        Log.i("Stub", "GameStateMain: Showing Main Screen UI");
    }
}