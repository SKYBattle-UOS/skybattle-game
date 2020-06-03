package com.example.Client;

import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerTargetSkill;
import Host.CoreHost;

public class GetYourHPClient extends PlayerTargetSkill {
    public GetYourHPClient(int index) {
        super();
    }

    @Override
    public String getName() {
        return "마인드스톤";
    }

    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        int yourHP = player.getProperty().getHealth()/1000;
        if (caster == Core.get().getMatch().getThisPlayer()){
            String targetName = Core.get()
                    .getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "의 체력은"+ yourHP + "입니다", 2);

        }
    }
}
