package Host;


import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;
import Common.SneakCommon;
import Common.Util;

public class SneakHost extends SneakCommon {

    @Override
    public void cast(GameObject caster){
        CoreHost.get().getMatch().getWorldSetterHost()
                .generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}
