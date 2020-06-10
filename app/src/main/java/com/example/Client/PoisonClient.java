package com.example.Client;

import Common.GameObject;
import Common.MatchCommon;
import Common.PlayerTargetSkillClient;
import Common.UIManager;

public class PoisonClient extends PlayerTargetSkillClient {
    public PoisonClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    public String getName(){
        return "독공격";
    }

    public void cast(GameObject caster){
        if (caster == Core.get().getMatch().getThisPlayer()) {
            String targetName = Core.get().getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "(을)를 독 공격 했습니다.");
            int btnIndex = Core.get().getUIManager().findButtonIndex(this);
            Core.get().getUIManager().setButtonActive(btnIndex, false);
        }
    }
}
