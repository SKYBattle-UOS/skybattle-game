package Host;

import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;
import Common.SuicideCommon;

public class SuicideHost extends SuicideCommon {
    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) caster;

        player.getProperty().setHealth(player.getProperty().getHealth() - 10000);

        WorldSetterHost wsh = CoreHost.get().getMatch().getWorldSetterHost();
        wsh.generateUpdateInstruction(player.getNetworkId(), PlayerProperty.healthDirtyFlag);
        wsh.generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}
