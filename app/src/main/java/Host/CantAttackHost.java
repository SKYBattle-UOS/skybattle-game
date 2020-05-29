package Host;
import com.example.Client.Core;
import Common.GameObject;
import Common.PlayerHost;

public class CantAttackHost extends CantAttackCommon {
    public CantAttackHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {
        todo(0);
        CoreHost.get().getMatch().setTimer(() -> todo(0),1);
    }

    public void todo(int value){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.setCantAttack(value);
        CoreHost.get()
                .getMatch()
                .getWorldSetterHost()
                .generateUpdateInstruction(player.getNetworkId(), PlayerHost.cantAttackFlag);

        CoreHost.get().getMatch().getWorldSetterHost().generateUpdateInstruction(player.getNetworkId(), PlayerHost.cantAttackFlag);
    }
}
