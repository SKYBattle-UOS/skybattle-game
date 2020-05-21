package com.example.Client;

import Common.GameObject;
import Common.PlayerCommon;

public class Player extends PlayerCommon {
    protected Player(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        setRenderComponent(Core.getInstance().getRenderer().createRenderComponent(this, ImageType.FILLED_CIRCLE));
    }

    public static GameObject createInstance() {
        return new Player(0, 0, "Player");
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
