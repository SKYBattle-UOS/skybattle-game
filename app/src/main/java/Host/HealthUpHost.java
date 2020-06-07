package Host;

import Common.GameObject;
import Common.HealthUpCommon;
import Common.PlayerHost;
import Common.PlayerProperty;

public class HealthUpHost extends HealthUpCommon {
    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.getProperty().setHealth(player.getProperty().getHealth() + 10000);
    }
}
