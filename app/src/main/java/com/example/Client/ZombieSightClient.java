package com.example.Client;

import Common.GameObject;
import Common.ImageType;
import Common.MatchCommon;
import Common.PlayerTargetSkillClient;
import Common.UIManager;

public class ZombieSightClient extends PlayerTargetSkillClient {
    private int _duration = 10;

    public ZombieSightClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "피의 흔적";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == ((Match) getMatch()).getThisPlayer()){
            PlayerClient target = (PlayerClient) getMatch().getRegistry().getGameObject(_networkId);
            target.setLook(ImageType.MARKER);
            displayDuration();
            getMatch().setTimer(this, () -> target.setLook(ImageType.INVISIBLE), 10);
            runCoolTime(30);
        }
    }

    private void displayDuration(){
        getUIManager().setTopText(String.format("피의 흔적 - %d초", _duration), 2);
        _duration--;

        if (_duration > 0){
            getMatch().setTimer(this, this::displayDuration, 1);
        }
        else {
            _duration = 10;
        }
    }
}
