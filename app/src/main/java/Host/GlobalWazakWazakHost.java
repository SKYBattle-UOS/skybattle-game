package Host;

import com.example.Client.ImageType;

import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.PlayerHost;
import Common.Util;

public class GlobalWazakWazakHost extends GlobalWazakWazakCommon {
    @Override
    public void cast(GameObject caster) {
        GameObject spawned = caster.getMatch().createGameObject(Util.ItemClassId, true);
        spawned.setName("원격 와작와작 지뢰");
        spawned.setPosition(_lat, _lon);
        spawned.setLook(ImageType.MARKER);
        spawned.getMatch().getWorldSetterHost().generateUpdateInstruction(caster.getNetworkId(), PlayerHost.shouldCastFlag);
    }
}
