package com.example.myapplication;

import android.graphics.Color;

/**
 * 임시 캐릭터 클래스. 볼품없는 스킬을 넣을 예정.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class TempPlayer extends GameObject {

    TempPlayer(String name) {
        super(0f, 0f, name);
    }

    @Override
    public void update(int ms) {

    }

    @Override
    public void render(Renderer renderer) {
        double[] position = getPosition();
        renderer.drawFilledCircle(position[0], position[1], Color.YELLOW, 10f);
    }
}
