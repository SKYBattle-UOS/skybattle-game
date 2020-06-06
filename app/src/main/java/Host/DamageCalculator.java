package Host;

import Common.Player;

public interface DamageCalculator {
    int calculateDamage(Player attacker, long ms);
}
