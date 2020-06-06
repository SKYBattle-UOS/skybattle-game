package Host;

import Common.Player;

public interface DamageApplier {
    void applyDamage(Player victim, Player attacker, int damage);
}
