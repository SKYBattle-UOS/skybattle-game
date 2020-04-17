package com.example.myapplication;

import android.graphics.Color;

public class TempPlayer extends GameObject {

    TempPlayer(String name) {
        super(0f, 0f, name);
    }

    @Override
    public void update(int ms) {

    }

    @Override
    public void render(Renderer renderer) {
        float[] position = getPosition();
        renderer.drawFilledCircle(position[0], position[1], Color.YELLOW, 10f);
    }
}
