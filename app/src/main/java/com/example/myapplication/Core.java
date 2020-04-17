package com.example.myapplication;

import android.util.Log;

public class Core {
    static Renderer renderer = new Renderer();
    static GameStateContext stateContext = new GameStateContext();

    public void run(){

        /* ===== DEBUG START ===== */

        int ms = 1000; // update every one second (1000 millisecond)

        stateContext.run(ms);
        stateContext.run(ms);

        // match start button pressed
        stateContext.switchState(GameStateType.MATCH);

        // match runs
        for (int i = 0; i < 30; i++)
            stateContext.run(ms);

        /* ===== DEBUG END ===== */
    }
}
