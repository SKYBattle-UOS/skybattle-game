package Host;

import Common.GameObject;
import Common.InstantSkill;
import Common.PlayerHost;
import Common.SuicideClient;

public class SuicideHost extends InstantSkillHost {
    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) caster;

        player.getProperty().setHealth(player.getProperty().getHealth() - 10000);
    }
}
