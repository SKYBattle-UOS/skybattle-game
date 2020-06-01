package Common;

import android.util.Log;

import com.example.Client.Core;

import Host.CoreHost;

public class DuplicationTrickCommon extends InstantSkill  {


    @Override
    public String getName() {
        return "분신술";
    }

    @Override
    public void cast(GameObject caster) {
        Player player = (Player) caster;
        Player anotherplayer = Core.get().getMatch().getThisPlayer();
            for (int i = 0; i < 3; i++) {
                GameObject spawned = CoreHost.get().getMatch().createGameObject(Util.ItemClassId, true);
                if (player.getProperty().getTeam() != anotherplayer.getProperty().getTeam())  //다른 팀이면
                    spawned.setName(caster.getName());
                else //같은 팀이면
                    spawned.setName(caster.getName()+"(가짜)");
                if (i == 0)
                    spawned.setPosition(caster.getPosition()[0] + Math.random() * 0.005, caster.getPosition()[1] - Math.random() * 0.005); //0.0 ~ 0.9999
                else if (i == 1)
                    spawned.setPosition(caster.getPosition()[0] + Math.random() * 0.005, caster.getPosition()[1] - Math.random() * 0.005);
                else
                    spawned.setPosition(caster.getPosition()[0] - Math.random() * 0.005, caster.getPosition()[1] + Math.random() * 0.005);
                spawned.setLook(ImageType.MARKER);
                CoreHost.get().getMatch().getWorldSetterHost().generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
                CoreHost.get().getMatch().setTimer(spawned::scheduleDeath, 10);
            }

    }
}
