package com.example.Client;
import Common.MatchCommon;
import Common.PlayerTargetSkillClient;

import Common.GameObject;
import Common.UIManager;

public class CantAttackClient extends PlayerTargetSkillClient {
    public CantAttackClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    public String getName(){
        return "공격 무력화";
    }

    public void cast(GameObject caster) {
        if (caster == ((Match) getMatch()).getThisPlayer()) {
            String targetName = getMatch().getRegistry().getGameObject(_networkId).getName();
            getUIManager().setTopText(targetName + "(을)를 공력을 10초 동안 무력화 했습니다.");
            int btnIndex = getUIManager().findButtonIndex(this);
            getUIManager().setButtonActive(btnIndex, false);
        }
    }
}
