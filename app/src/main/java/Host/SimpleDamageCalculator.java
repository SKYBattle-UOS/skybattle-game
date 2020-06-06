package Host;

import Common.Player;

public class SimpleDamageCalculator implements DamageCalculator {
    @Override
    public int calculateDamage(Player attacker, long ms) {
        return attacker.getProperty().getDPS() * ms / 1000;
    }
}
