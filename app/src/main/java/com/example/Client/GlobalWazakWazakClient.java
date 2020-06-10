package com.example.Client;

import Common.CoordinateSkillClient;
import Common.GameObject;
import Common.MatchCommon;

public class GlobalWazakWazakClient extends CoordinateSkillClient {
    public GlobalWazakWazakClient(MatchCommon match) {
        super(match);
    }

    @Override
    public String getName() {
        return "원격 와작와작 뻥!";
    }

    @Override
    public void cast(GameObject caster) {
        runCoolTime(100, Core.get().getUIManager());
    }
}