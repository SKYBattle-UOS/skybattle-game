package Host;
import Common.Damageable;
import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;
import Common.PlayerTargetSkill;

public class ReflectDamageHost extends ReflectDamageCommon {
    @Override
    public void cast(GameObject caster) {
        MatchHost match = CoreHost.get().getMatch();
        PlayerHost castingPlayer = (PlayerHost) caster;

        Damageable target = (Damageable) match.getRegistry().getGameObject(_networkId);
        DamageApplier originalDA = castingPlayer.getDamageApplier();
        castingPlayer.setDamageApplier(new ReverseDamageApplier(target, originalDA));

        match.getWorldSetterHost()
                .generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);

        match.setTimer(() -> castingPlayer.setDamageApplier(originalDA), 3f);
    }
}
