package Host;

import com.example.Client.ImageType;

import Common.GameObject;
import Common.GlobalWazakWazakCommon;
import Common.Pickable;
import Common.PlayerHost;
import Common.Util;

public class GlobalWazakWazakHost extends GlobalWazakWazakCommon {
    @Override
    public void cast(GameObject caster) {
        MatchHost match = CoreHost.get().getMatch();

        GameObject spawned = match.createGameObject(Util.ItemClassId, true);
        spawned.setName("원격 와작와작 지뢰");
        spawned.setPosition(_lat, _lon);
        spawned.setLook(ImageType.MARKER);

        match.getWorldSetterHost()
                .generateUpdateInstruction(caster.getNetworkId(), PlayerHost.skillDirtyFlag);
        match.setTimer(() -> {
            if (!((Pickable) spawned).isPickedUp())
                spawned.scheduleDeath();
        }, 10);
    }
}
