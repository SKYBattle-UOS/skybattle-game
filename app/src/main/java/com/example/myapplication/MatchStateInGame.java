package com.example.myapplication;

import android.util.Log;

public class MatchStateInGame implements GameState {
    private GameStateMatch _match;

    MatchStateInGame(GameStateMatch gameStateMatch) {
        _match = gameStateMatch;
    }

    @Override
    public void run(int ms) {
        Log.i("Stub", "MatchStateInGame: Game is Playing");

        GameObject[] gameObjects = _match.getGameObjects();
        for (GameObject go : gameObjects)
           go.update(ms);
    }
}
