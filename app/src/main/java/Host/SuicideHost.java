package Host;

import Common.GameObject;
import Common.InstantSkillHost;
import Common.MatchCommon;
import Common.UIManager;

public class SuicideHost extends InstantSkillHost {
    public SuicideHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) caster;

        player.getProperty().setHealth(player.getProperty().getHealth() - 10000);
    }
}
