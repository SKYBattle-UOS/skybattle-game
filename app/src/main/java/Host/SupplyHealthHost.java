package Host;

import Common.CoordinateSkillHost;
import Common.ImageType;

import Common.GameObject;
import Common.ItemHost;
import Common.MatchCommon;
import Common.UIManager;
import Common.Util;

public class SupplyHealthHost extends CoordinateSkillHost {
    public SupplyHealthHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster) {
        MatchHost match = CoreHost.get().getMatch();

        ItemHost spawned = (ItemHost) match.createGameObject(Util.ItemClassId, true);
        spawned.setName("포션");
        spawned.setPosition(_lat, _lon);
        spawned.setLook(ImageType.MARKER);
    }
}
