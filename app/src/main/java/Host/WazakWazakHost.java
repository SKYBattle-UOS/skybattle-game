package Host;

import com.example.Client.ImageType;

import Common.GameObject;
import Common.WazakWazakCommon;
import Common.Util;

public class WazakWazakHost extends WazakWazakCommon {
    @Override
    public void cast(GameObject caster){
        GameObject spawned = caster.getMatch().createGameObject(Util.ItemClassId, true);
        spawned.setName("와작와작 지뢰");
        spawned.setPosition(caster.getPosition()[0], caster.getPosition()[1]);
        spawned.setLook(ImageType.MARKER);
    }
}
