package Host;
import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;

public class CantAttackHost extends CantAttackCommon {
    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        DamageCalculator originalDC = player.getDamageCalculator();
        player.setDamageCalculator(new ZeroDamageCalculator());

        CoreHost.get().getMatch()
                .getWorldSetterHost()
                .generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);

        CoreHost.get().getMatch().setTimer(() -> player.setDamageCalculator(originalDC), 3f);
    }
}
