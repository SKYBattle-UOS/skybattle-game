package Host;
import Common.GameObject;
import Common.PlayerHost;

public class ReflectDamageHost extends CantAttackCommon {
    public ReflectDamageHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {
        todo(true);
        CoreHost.get().getMatch().setTimer(() -> todo(false),10);
    }

    public void todo(boolean value){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.getProperty().setReflectAttack(value);

        CoreHost.get().getMatch().getWorldSetterHost().generateUpdateInstruction(player.getNetworkId(), player.getProperty().reflectAttackFlag);
    }
}
