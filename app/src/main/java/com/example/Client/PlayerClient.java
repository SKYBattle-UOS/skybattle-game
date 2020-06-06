package com.example.Client;

import Common.GameObject;
import Common.InputBitStream;
import Common.Item;
import Common.Player;
import Common.PlayerProperty;
import Common.Skill;

public class PlayerClient extends GameObjectClient implements Player {
    public static class Friend {
        private Friend(){}
    }

    private static final Friend friend = new Friend();

    private PlayerProperty _property = new PlayerProperty(this){
        @Override
        public void setHealth(int health) {
            super.setHealth(health);
            onSetHealth(health);
        }
    };

    private PlayerStateBase _playerState = new PlayerStateBase(this);
    private boolean _shouldChangeState;

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
        if (_shouldChangeState){
            changeState();
            _shouldChangeState = false;
        }

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

        _playerState.update(ms);
    }

    @Override
    public void after(long ms) {
    }

    @Override
    public void onItemsDirty() {
        if (Core.get().getMatch().getThisPlayer() == this)
            Core.get().getUIManager().updateItems();
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

    @Override
    public void onPlayerStateChange(PlayerState state){
        _shouldChangeState = true;
    }

    private void onSetHealth(int health) {
        if (Core.get().getMatch().getThisPlayer() == this)
            Core.get().getUIManager().setHealth(health);
    }


    private void changeState(){
        _playerState.finish();

        PlayerState state = getProperty().getPlayerState();
        switch (state){
            case NORMAL:
                _playerState = new PlayerStateNormal(this);
                break;

            case GHOST:
                _playerState = new PlayerStateGhost(this);
                break;
        }

        _playerState.start();
    }
}