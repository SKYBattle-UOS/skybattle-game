package com.example.Client;

import Common.GameObject;
import Common.InstantSkillClient;
import Common.MatchCommon;
import Common.UIManager;

public class SuicideClient extends InstantSkillClient {
    public SuicideClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "자살";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == Core.get().getMatch().getThisPlayer()){
            Core.get().getUIManager().setTopText("체력을 감소했습니다", 3);
            runCoolTime(3);
        }
    }
}
