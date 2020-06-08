package Host;

import Common.CoordinateSkillHost;
import Common.ImageType;

import Common.GameObject;
import Common.ItemHost;
import Common.MatchCommon;
import Common.Util;

public class GlobalWazakWazakHost extends CoordinateSkillHost {
    public GlobalWazakWazakHost(MatchCommon match) {
        super(match);
    }

    @Override
    public void cast(GameObject caster) {
        MatchHost match = CoreHost.get().getMatch();

        ItemHost spawned = (ItemHost) match.createGameObject(Util.ItemClassId, true);
        spawned.setName("원격 와작와작 지뢰");
        spawned.setPosition(_lat, _lon);
        spawned.setLook(ImageType.MARKER);

        match.setTimer(() -> {
            if (!spawned.isPickedUp())
                spawned.scheduleDeath();
        }, 10);
    }
}
