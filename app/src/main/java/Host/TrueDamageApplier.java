package Host;

import Common.Player;
import Common.PlayerProperty;

public class TrueDamageApplier implements DamageApplier {
    @Override
    public void applyDamage(Player victim, Player attacker, int damage) {
        PlayerProperty victimProperty = victim.getProperty();
        victimProperty.setHealth(victimProperty.getHealth() - damage);
    }
}
