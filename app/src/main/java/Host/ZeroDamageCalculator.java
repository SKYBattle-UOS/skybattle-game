package Host;

import Common.Player;

public class ZeroDamageCalculator implements DamageCalculator {
    @Override
    public int calculateDamage(Player attacker, long ms) {
        return 0;
    }
}
