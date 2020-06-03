package com.example.Client;

import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerTargetSkill;
import Common.Util;
import Host.CoreHost;
import Host.GetDistanceHost;

public class GetDistanceClient extends PlayerTargetSkill {
    public GetDistanceClient(int index) {
        super();
    }

    @Override
    public String getName() {
        return "오라클";
    }

    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        double distance = Util.distanceBetweenLatLon(player.getPosition()[0], player.getPosition()[1], caster.getPosition()[0], caster.getPosition()[1]);
        if (caster == Core.get().getMatch().getThisPlayer()){
            String targetName = Core.get()
                    .getMatch().getRegistry().getGameObject(_networkId).getName();

            Core.get().getUIManager().setTopText(targetName + "(와)과의 거리는"+ distance + "입니다", 3);
        }
    }
}
