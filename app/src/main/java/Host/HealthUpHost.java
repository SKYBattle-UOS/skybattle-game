package Host;

import Common.GameObject;
import Common.HealthUpClient;
import Common.PlayerHost;

public class HealthUpHost extends PlayerTargetSkillHost {
    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.getProperty().setHealth(player.getProperty().getHealth() + 10000);
    }
}
