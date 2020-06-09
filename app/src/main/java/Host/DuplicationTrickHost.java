package Host;

import Common.ImageType;
import Common.GameObject;
import Common.InstantSkillHost;
import Common.MatchCommon;
import Common.Util;

public class DuplicationTrickHost extends InstantSkillHost {
    public DuplicationTrickHost(MatchCommon match) {
        super(match);
    }

    @Override
    public void cast(GameObject caster) {
        for(int i=0;i<3;i++) {
            GameObject spawned = CoreHost.get().getMatch().createGameObject(Util.ItemClassId, true);
            spawned.setName(caster.getName()+"(가짜)");
            if (i == 0)
                spawned.setPosition(caster.getPosition()[0] + Math.random()*0.005, caster.getPosition()[1] -Math.random()*0.005); //0.0 ~ 0.9999
            else if (i == 1)
                spawned.setPosition(caster.getPosition()[0] +Math.random()*0.005, caster.getPosition()[1] - Math.random()*0.005);
            else
                spawned.setPosition(caster.getPosition()[0] -Math.random()*0.005, caster.getPosition()[1] +Math.random()*0.005);
            spawned.setLook(ImageType.INVISIBLE);
            CoreHost.get().getMatch().setTimer(spawned::scheduleDeath,60);
        }
    }
}
