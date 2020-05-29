package Host;
import com.example.Client.Core;

import Common.GameObject;
import Common.PlayerHost;

public class PosionHost extends HealthUpCommon {
    public PosionHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {
        for (int i= 0; i <10; i++)
        {
            CoreHost.get().getMatch().setTimer(() -> todo(),1);
        }
    }

    public void todo(){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.setHealth(player.getHealth() - 5000);

        CoreHost.get()
                .getMatch()
                .getWorldSetterHost()
                .generateUpdateInstruction(player.getNetworkId(), PlayerHost.healthDirtyFlag);

        CoreHost.get().getMatch().getWorldSetterHost().generateUpdateInstruction(player.getNetworkId(), PlayerHost.skillDirtyFlag);
    }
}
