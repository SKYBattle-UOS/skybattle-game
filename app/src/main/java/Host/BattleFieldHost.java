package Host;

import java.util.Collection;

import Common.CollisionState;
import Common.Damageable;
import Common.Player;

public class BattleFieldHost extends GameObjectHost {
    private int _dps;

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
        for (Player p : _match.getPlayers()){
            Collection<CollisionState> collisions = _match
                    .getCollider().getCollisions(p.getGameObject());

            boolean isInside = false;
            for (CollisionState colli : collisions){
                if (colli.other == this){
                    isInside = true;
                    break;
                }
            }

            if (isInside)
                continue;

            int damage = (int) (_dps * ms / 1000);
            ((Damageable) p).takeDamage(this, damage);
        }
    }

    @Override
    public void after(long ms) {

    }

    public void setDPS(int dps){
        _dps = dps;
    }
}
