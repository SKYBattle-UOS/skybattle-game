package Host;

import Common.ImageType;

import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.ItemHost;
import Common.Pickable;
import Common.PlayerProperty;
import Common.Util;

public class GlobalWazakWazakHost extends GlobalWazakWazakCommon {
    @Override
    public void cast(GameObject caster) {
        MatchHost match = CoreHost.get().getMatch();

        ItemHost spawned = (ItemHost) match.createGameObject(Util.ItemClassId, true);
        spawned.setName("원격 와작와작 지뢰");
        spawned.setPosition(_lat, _lon);
        spawned.setLook(ImageType.MARKER);

        match.getWorldSetterHost()
                .generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
        match.setTimer(() -> {
            if (!spawned.isPickedUp())
                spawned.scheduleDeath();
        }, 10);
    }
}
