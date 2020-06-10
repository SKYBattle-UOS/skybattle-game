package Host;
import Common.Damageable;
import Common.GameObject;
import Common.MatchCommon;
import Common.PlayerTargetSkillHost;
import Common.UIManager;

public class ReflectDamageHost extends PlayerTargetSkillHost {
    public ReflectDamageHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster) {
        MatchHost match = CoreHost.get().getMatch();
        PlayerHost castingPlayer = (PlayerHost) caster;

        Damageable target = (Damageable) match.getRegistry().getGameObject(_networkId);
        DamageApplier originalDA = castingPlayer.getDamageApplier();
        castingPlayer.setDamageApplier(new ReverseDamageApplier(target, originalDA));

        match.setTimer(this, () -> castingPlayer.setDamageApplier(originalDA), 3f);
    }
}
