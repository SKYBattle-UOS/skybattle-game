package com.example.Client;

import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.InputBitStream;
import Common.Player;
import Common.PlayerProperty;
import Common.Skill;
import Common.WazakWazakCommon;
import Common.HealthUpCommon;

public class PlayerClient extends GameObjectClient implements Player {
    private PlayerProperty _property = new PlayerProperty(){
        @Override
        public void setHealth(int health) {
            super.setHealth(health);
            onSetHealth(health);
        }
    };

    protected PlayerClient(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _property.getSkills().set(0, new WazakWazakCommon());
        _property.getSkills().set(1, new GlobalWazakWazakCommon());
        _property.getSkills().set(2, new HealthUpCommon());
        _property.getSkills().set(3, new HealthUpCommon());
    }

    public static GameObject createInstance() {
        return new PlayerClient(0, 0, "Player");
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);
        _property.readFromStream(stream, dirtyFlag);
    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
        for (Skill skill : _property.getSkills())
            if (skill.isDirty()){
                skill.cast(this);
                skill.setDirty(false);
            }
    }

    @Override
    public void after(long ms) {

    }

    @Override
    protected void onItemsAdded() {
        if (Core.get().getMatch().getThisPlayer() == this)
            Core.get().getUIManager().updateItems();

        while (_property.getSkills().size() > 4)
            _property.getSkills().remove(_property.getSkills().size() - 1);

        for (int i = 0; i < getItems().size(); i++)
            _property.getSkills().add(getItems().get(i).getProperty().getSkill());
    }

    @Override
    public GameObject getGameObject() {
        return this;
    }

    @Override
    public PlayerProperty getProperty(){
        return _property;
    }

    private void onSetHealth(int health) {
        if (Core.get().getMatch().getThisPlayer() == this)
            Core.get().getUIManager().setHealth(health);
    }
}