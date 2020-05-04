package com.example.myapplication;

import android.graphics.Color;
import android.util.Log;

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
    public void update(long ms) {

    }

    @Override
    public void render(Renderer renderer, long ms) {
        double[] position = getPosition();
        renderer.drawFilledCircle(position[0], position[1], Color.YELLOW, 10f);
    }

    public static GameObject createInstance() {
        return new TempPlayer("Temp Player");
    }

    @Override
    public void writeToStream(OutputBitStream stream) {
        // TODO
    }

    @Override
    public void readFromStream(InputBitStream stream) {
        // TODO
        byte[] b = new byte[2];
        stream.readBytes(b, 8);

        if (b[0] == 1){
            stream.readBytes(b, 16);
            setPosition(b[0], b[1]);
        }
    }

    @Override
    public void faceDeath(){
        Log.i("Stub", "TempPlayer: " + getName() + " is dying");
    }
}
