package Host;

import Common.ImageType;

import Common.GameObject;
import Common.InstantSkill;
import Common.SpiderMineClient;
import Common.Util;

public class SpiderMineHost extends InstantSkill {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void cast(GameObject caster){
        GameObject spawned = CoreHost.get().getMatch().createGameObject(Util.ItemClassId, true);
        spawned.setName("스파이더마인");
        spawned.setPosition(caster.getPosition()[0], caster.getPosition()[1]);
        spawned.setLook(ImageType.MARKER);
    }
}
