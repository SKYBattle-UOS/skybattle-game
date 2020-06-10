package Host;

import Common.GameObject;
import Common.MatchCommon;
import Common.PlayerTargetSkillHost;
import Common.UIManager;

public class ZombieSightHost extends PlayerTargetSkillHost {
    public ZombieSightHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster) {
        // nothing
    }
}
