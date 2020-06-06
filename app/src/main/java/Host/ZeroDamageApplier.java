package Host;

import Common.Player;

public class ZeroDamageApplier implements DamageApplier {
    @Override
    public void applyDamage(Player victim, Player attacker, int damage) {
        // nothing
    }
}
