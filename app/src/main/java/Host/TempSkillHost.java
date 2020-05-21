package Host;

import Common.GameObject;
import Common.TempSkillCommon;
import Common.Util;

public class TempSkillHost extends TempSkillCommon {
    @Override
    public void cast(GameObject caster){
        GameObject spawned = caster.getMatch().createGameObject(Util.ItemClassId);
        spawned.setName("와작와작 지뢰");
        spawned.setPosition(caster.getPosition()[0], caster.getPosition()[1]);
    }
}
