package Common;

public interface Damageable {
    void takeDamage(GameObject attacker, int damage);
    int getTeam();
}
