package Host;

import Common.GameObject;
import Common.InstantSkillHost;
import Common.MatchCommon;
import Common.UIManager;

public class ZombieSensorHost extends InstantSkillHost {
    public ZombieSensorHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster) {
        // nothing
    }
}
