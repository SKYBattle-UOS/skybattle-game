package Host;
import Common.GameObject;
import Common.PlayerHost;

public class PosionHost extends HealthUpCommon {
    public PosionHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {
        for (int i= 0; i <10; i++)  // 주기적으로 어떻게 변경해야하는가?
        {
            PlayerHost player = (PlayerHost) CoreHost.getInstance()
                    .getMatch().getRegistry().getGameObject(_networkId);

            player.setHealth(player.getHealth() - 5000);

            CoreHost.getInstance()
                    .getMatch()
                    .getWorldSetterHost()
                    .generateUpdateInstruction(player.getNetworkId(), PlayerHost.healthDirtyFlag);

            player.getMatch().getWorldSetterHost().generateUpdateInstruction(caster.getNetworkId(), PlayerHost.skillDirtyFlag);
        }
    }
}
