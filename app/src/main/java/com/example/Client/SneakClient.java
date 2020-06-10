package com.example.Client;

import Common.GameObject;
import Common.ImageType;
import Common.InstantSkillClient;
import Common.MatchCommon;
import Common.Player;
import Common.UIManager;


public class SneakClient extends InstantSkillClient {
    public SneakClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "은신";
    }

    @Override
    public void cast(GameObject caster) {
        Player player = (Player)caster;
        Player anotherplayer = Core.get().getMatch().getThisPlayer();
        if (player.getProperty().getTeam() != anotherplayer.getProperty().getTeam()) { //다른  팀이면
            caster.setLook(ImageType.INVISIBLE);
            Core.get().getMatch().setTimer(this,
                    () -> caster.setLook(ImageType.MARKER), 60); //60초뒤 다시 marker

        }
    }
}

