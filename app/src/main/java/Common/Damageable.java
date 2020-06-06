package Common;

public interface Damageable {
    void takeDamage(Player attacker, int damage);
    int getTeam();
}
