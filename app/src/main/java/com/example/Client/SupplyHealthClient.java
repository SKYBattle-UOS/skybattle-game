package com.example.Client;

import Common.CoordinateSkillClient;
import Common.GameObject;
import Common.MatchCommon;
import Common.UIManager;

public class SupplyHealthClient extends CoordinateSkillClient {
    public SupplyHealthClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "보급품 투하";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == ((Match) getMatch()).getThisPlayer())
            runCoolTime(30);
    }
}
