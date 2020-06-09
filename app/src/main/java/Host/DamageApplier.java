package Host;

import Common.GameObject;
import Common.Player;

public interface DamageApplier {
    void applyDamage(Player victim, GameObject attacker, int damage);
}
