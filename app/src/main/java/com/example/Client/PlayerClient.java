package com.example.Client;

import Common.GameObject;
import Common.InputBitStream;
import Common.Item;
import Common.Player;
import Common.PlayerProperty;
import Common.IngameInfoListener;
import Common.Skill;

public class PlayerClient extends GameObjectClient implements Player {
    private PlayerProperty _property = new PlayerProperty(){
        @Override
        public void setHealth(int health) {
            super.setHealth(health);
            _ingameInfoListener.onHealthChange(health);
        }

        @Override
        public void setPlayerState(PlayerState state) {
            if (state == PlayerState.ZOMBIE)
                _match.getCharacterFactory().setCharacterProperty(PlayerClient.this, 1);
            super.setPlayerState(state);
            _ingameInfoListener.onPlayerStateChange(state);
        }
    };

    // dummy
    private IngameInfoListener _ingameInfoListener = new IngameInfoListener() {
        @Override
        public void onPlayerStateChange(PlayerState state) {
        }

        @Override
        public void onItemsChange() {
        }

        @Override
        public void onHealthChange(int health) {
        }
    };

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

        for (Item item : getItems()){
            Skill skill = item.getProperty().getSkill();
            if (skill.isDirty()){
                skill.cast(this);
                skill.setDirty(false);
            }
        }
    }

    @Override
    public void after(long ms) {
    }

    @Override
    public void onItemsDirty() {
        _ingameInfoListener.onItemsChange();
    }

    @Override
    public GameObject getGameObject() {
        return this;
    }

    @Override
    public PlayerProperty getProperty(){
        return _property;
    }

    @Override
    public void setProperty(PlayerProperty property) {
        _property.getFromFactory(property);
    }

    public void setIngameInfoListener(IngameInfoListener listener){
        _ingameInfoListener = listener;
    }
}