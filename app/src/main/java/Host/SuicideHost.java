package Host;

import Common.GameObject;
import Common.InstantSkillHost;
import Common.PlayerHost;

public class SuicideHost extends InstantSkillHost {
    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) caster;

        player.getProperty().setHealth(player.getProperty().getHealth() - 10000);
    }
}
