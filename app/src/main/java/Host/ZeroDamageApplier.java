package Host;

import Common.GameObject;
import Common.Player;

public class ZeroDamageApplier implements DamageApplier {
    @Override
    public void applyDamage(Player victim, GameObject attacker, int damage) {
        // nothing
    }
}
