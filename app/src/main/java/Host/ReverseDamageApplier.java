package Host;

import Common.Damageable;
import Common.GameObject;
import Common.Player;

public class ReverseDamageApplier implements DamageApplier {
    private Damageable mTarget;
    private DamageApplier mIfNotTarget;

    public ReverseDamageApplier(Damageable target, DamageApplier defaultApplier){
        mTarget = target;
        mIfNotTarget = defaultApplier;
    }

    @Override
    public void applyDamage(Player victim, GameObject attacker, int damage) {
        if (attacker == mTarget)
            mTarget.takeDamage((GameObject) victim, damage);
        else
            mIfNotTarget.applyDamage(victim, attacker, damage);
    }
}
