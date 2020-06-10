package Host;

import Common.GameObject;
import Common.InstantSkillHost;
import Common.MatchCommon;
import Common.UIManager;

public class SneakHost extends InstantSkillHost {
    public SneakHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster){
    }
}
