package Host;

import Common.GameObject;
import Common.PlayerHost;

public class HealthUpHost extends HealthUpCommon {
    public HealthUpHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.setHealth(player.getHealth() + 10000);

        CoreHost.get()
                .getMatch()
                .getWorldSetterHost()
                .generateUpdateInstruction(player.getNetworkId(), PlayerHost.healthDirtyFlag);

        player.getMatch().getWorldSetterHost().generateUpdateInstruction(caster.getNetworkId(), PlayerHost.skillDirtyFlag);
    }
}
