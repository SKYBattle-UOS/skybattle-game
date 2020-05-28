package com.example.Client;

import Common.DuplicationTrickCommon;
import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.PlayerCommon;
import Common.Skill;
import Common.SneakCommon;
import Common.WazakWazakCommon;
import Host.HealthUpCommon;

public class Player extends PlayerCommon {
    protected Player(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _skills[0] = new SneakCommon(0);//new WazakWazakCommon(0);
        _skills[1] = new DuplicationTrickCommon(1);//new GlobalWazakWazakCommon(1);
        _skills[2] = new HealthUpCommon(2);
        _skills[3] = new HealthUpCommon(3);
    }

    public static GameObject createInstance() {
        return new Player(0, 0, "Player");
    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
        for (Skill skill : _skills)
            if (skill.isDirty()){
                skill.cast(this);
                skill.setDirty(false);
            }
    }

    @Override
    public void after(long ms) {

    }

    @Override
    public void setHealth(int health) {
        super.setHealth(health);
        Core.getInstance().getUIManager().setHealth(health);
    }

    @Override
    public void faceDeath() {
        super.faceDeath();
        InputManager input = Core.getInstance().getInputManager();
        if (this == input.getThisPlayer())
            input.setThisPlayer(null);
    }
}
