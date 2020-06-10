package com.example.Client;
import Common.MatchCommon;
import Common.PlayerTargetSkillClient;

import Common.GameObject;
import Common.UIManager;

public class ReflectDamageClient extends PlayerTargetSkillClient {
    public ReflectDamageClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    public String getName(){
        return "공격 반사";
    }

    public void cast(GameObject caster){
        if (caster == Core.get().getMatch().getThisPlayer()) {
            String targetName = Core.get().getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "(을)를 받는 공격력을 5초 동안 반사 했습니다.");
            int btnIndex = Core.get().getUIManager().findButtonIndex(this);
            Core.get().getUIManager().setButtonActive(btnIndex, false);
        }
    }
}
