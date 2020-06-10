package com.example.Client;

import Common.GameObject;
import Common.InstantSkillClient;
import Common.MatchCommon;
import Common.UIManager;

public class SpiderMineClient extends InstantSkillClient {
    public SpiderMineClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "스파이더 마인";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster != Core.get().getMatch().getThisPlayer()) return;

        Core.get().getUIManager().setTopText("스파이더 마인을 설치했습니다", 3f);
        runCoolTime(10);
    }
}
