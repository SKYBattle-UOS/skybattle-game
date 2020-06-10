package com.example.Client;

import Common.GameObject;
import Common.ImageType;
import Common.InstantSkillClient;
import Common.MatchCommon;
import Common.Player;
import Common.ReadOnlyList;
import Common.UIManager;

public class DuplicationTrickClient extends InstantSkillClient {
    public DuplicationTrickClient(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "분신술";
    }

    @Override
    public void cast(GameObject caster) {
        Player player = (Player) caster;
        Player anotherplayer = Core.get().getMatch().getThisPlayer();
        ReadOnlyList<GameObject> gameObjects= Core.get().getMatch().getWorld();
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject spawned = gameObjects.get(i);
                if(spawned.getName().equals(caster.getName()+"(가짜)")) {
                    if (player.getProperty().getTeam() != anotherplayer.getProperty().getTeam()) {  //다른 팀이면
                        spawned.setName(caster.getName());
                    }
                    spawned.setLook(ImageType.MARKER);
                }
            }

    }
}
