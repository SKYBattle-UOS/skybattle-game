package com.example.Client;

import Common.CoordinateSkillClient;
import Common.GameObject;
import Common.MatchCommon;
import Common.UIManager;

public class GlobalWazakWazakClient extends CoordinateSkillClient {
    public GlobalWazakWazakClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "원격 와작와작 뻥!";
    }

    @Override
    public void cast(GameObject caster) {
        runCoolTime(100);
    }
}
