package com.example.Client;

import Common.DuplicationTrickCommon;
import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.InputBitStream;
import Common.Player;
import Common.PlayerProperty;
import Common.Skill;
import Common.SuicideCommon;
import Common.SneakCommon;
import Common.WazakWazakCommon;
import Common.HealthUpCommon;
import Host.WazakWazakHost;

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

    private boolean _reconstructSkills;
    private PlayerStateBase _playerState = new PlayerStateBase(this);
    private boolean _shouldChangeState;

    public PlayerClient() {
        _property.getSkills(friend).set(0, new WazakWazakCommon());
        _property.getSkills(friend).set(1, new GlobalWazakWazakCommon());
        _property.getSkills(friend).set(2, new HealthUpCommon());
        _property.getSkills(friend).set(3, new SuicideCommon());
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
        if (_shouldChangeState){
            changeState();
            _shouldChangeState = false;
        }

        for (Skill skill : _property.getSkills())
            if (skill.isDirty()){
                skill.cast(this);
                skill.setDirty(false);
            }

        if (_reconstructSkills){
            reconstructSkills();
            _reconstructSkills = false;
        }

        _playerState.update(ms);
    }

    @Override
    public void after(long ms) {
    }

    @Override
    public void onItemsDirty() {
        _reconstructSkills = true;
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
        _property.move(property);
    }

    @Override
    public void onPlayerStateChange(PlayerState state){
        _shouldChangeState = true;
    }

    private void reconstructSkills(){
        if (Core.get().getMatch().getThisPlayer() == this)
            Core.get().getUIManager().updateItems();

        while (_property.getSkills().size() > 4)
            _property.getSkills(friend).remove(_property.getSkills().size() - 1);

        for (int i = 0; i < getItems().size(); i++)
            _property.getSkills(friend).add(getItems().get(i).getProperty().getSkill());
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