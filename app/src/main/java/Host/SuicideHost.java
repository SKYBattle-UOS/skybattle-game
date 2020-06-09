package Host;

import Common.GameObject;
import Common.InstantSkillHost;
import Common.MatchCommon;

public class SuicideHost extends InstantSkillHost {
    public SuicideHost(MatchCommon match) {
        super(match);
    }

    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) caster;

        player.getProperty().setHealth(player.getProperty().getHealth() - 10000);
    }
}
