package com.example.Client;

import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.PlayerCommon;
import Common.Skill;
import Common.WazakWazakCommon;
import Host.HealthUpCommon;

public class Player extends PlayerCommon {
    protected Player(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _skills.set(0, new WazakWazakCommon());
        _skills.set(1, new GlobalWazakWazakCommon());
        _skills.set(2, new HealthUpCommon());
        _skills.set(3, new HealthUpCommon());
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
        Core.get().getUIManager().setHealth(health);
    }

    @Override
    public void faceDeath() {
        super.faceDeath();
    }

    @Override
    protected void itemsWereAdded() {
        if (Core.get().getMatch().getThisPlayer() == this)
            Core.get().getUIManager().updateItems();
    }
}
