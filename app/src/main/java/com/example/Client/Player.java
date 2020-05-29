package com.example.Client;

import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.PlayerCommon;
import Common.WazakWazakCommon;

public class Player extends PlayerCommon {
    protected Player(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _skills[0] = new WazakWazakCommon();
        _skills[1] = new GlobalWazakWazakCommon();
        _skills[2] = new HealthUp();
        _skills[3] = new HealthUp();
    }

    public static GameObject createInstance() {
        return new Player(0,0 , "Player");
    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
    }

    @Override
    public void after(long ms) {

    }
}
