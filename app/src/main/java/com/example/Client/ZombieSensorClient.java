package com.example.Client;

import Common.GameObject;
import Common.InstantSkillClient;
import Common.MatchCommon;
import Common.Player;
import Common.UIManager;
import Common.Util;

public class ZombieSensorClient extends InstantSkillClient {
    public ZombieSensorClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "좀비의 감";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == ((Match) getMatch()).getThisPlayer()){
            float dist = 10000f;
            for (Player p : getMatch().getPlayers()){
                if (p.getProperty().getPlayerState() == PlayerState.NORMAL){
                    dist = Math.min(dist, Util.distanceBetweenLatLon(
                            caster.getPosition(), p.getGameObject().getPosition()));
                }
            }

            getUIManager().setTopText(String.format("%d미터 안에 인간이 있다...", (int) dist), 3f);
            runCoolTime(10);
        }
    }
}
