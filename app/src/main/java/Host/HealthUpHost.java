package Host;

import Common.GameObject;
import Common.PlayerHost;

public class HealthUpHost extends HealthUpCommon {
    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.getInstance()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.setHealth(player.getHealth() + 10000);

        CoreHost.getInstance()
                .getMatch()
                .getWorldSetterHost()
                .generateUpdateInstruction(player.getNetworkId(), 1 << 4);
    }
}
