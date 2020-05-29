package Host;

import Common.GameObject;
import Common.PlayerHost;

public class HealthUpHost extends HealthUpCommon {
    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.setHealth(player.getHealth() + 10000);

        WorldSetterHost wsh = CoreHost.get().getMatch().getWorldSetterHost();
        wsh.generateUpdateInstruction(player.getNetworkId(), PlayerHost.healthDirtyFlag);
        wsh.generateUpdateInstruction(caster.getNetworkId(), PlayerHost.skillDirtyFlag);
    }
}
