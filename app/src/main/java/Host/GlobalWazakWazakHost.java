package Host;

import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.Util;

public class GlobalWazakWazakHost extends GlobalWazakWazakCommon {
    @Override
    public void cast(GameObject caster) {
        GameObject spawned = caster.getMatch().createGameObject(Util.ItemClassId);
        spawned.setName("원격 와작와작 지뢰");
        spawned.setPosition(_lat, _lon);
    }
}
