package com.example.Client;
import Common.MatchCommon;
import Common.PlayerTargetSkillClient;

import Common.GameObject;

public class CantAttackClient extends PlayerTargetSkillClient {
    public CantAttackClient(MatchCommon match) {
        super(match);
    }

    public String getName(){
        return "공격 무력화";
    }

    public void cast(GameObject caster) {
        if (caster == Core.get().getMatch().getThisPlayer()) {
            String targetName = Core.get().getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "(을)를 공력을 10초 동안 무력화 했습니다.");
            int btnIndex = Core.get().getUIManager().findButtonIndex(this);
            Core.get().getUIManager().setButtonActive(btnIndex, false);
        }
    }
}
