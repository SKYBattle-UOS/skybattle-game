package Host;
import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;

public class CantAttackHost extends CantAttackCommon {
    public CantAttackHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {
        todo(caster,true);
        CoreHost.get().getMatch().setTimer(() -> todo(caster,false),10);
    }

    public void todo(GameObject caster, boolean value){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.getProperty().setCantAttack(value);

        CoreHost.get().getMatch()
                .getWorldSetterHost()
                .generateUpdateInstruction(player.getNetworkId(), player.getProperty().cantAttackFlag);

        WorldSetterHost wsh = CoreHost.get().getMatch().getWorldSetterHost();
        wsh.generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}
