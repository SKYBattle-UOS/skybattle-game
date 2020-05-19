package Common;

import java.util.ArrayList;

public class Player extends GameObject implements Damageable {
    private int _playerId;
    private int _health = 100000;
    private int _dps = 20000;
    private int _team;
    private ArrayList<GameObject> _inCollision;

    public Player(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _inCollision = new ArrayList<>();
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);
    }

    @Override
    public void update(long ms) {
        doDamage(ms);
    }

    @Override
    public void collisionEnter(GameObject other){
        if (!_inCollision.contains(other))
            _inCollision.add(other);
    }

    @Override
    public void collisionExit(GameObject other){
        _inCollision.remove(other);
    }

    @Override
    public void getHurt(int damage) {
        // 0 defense
        setHealth(getHealth() - damage);
    }

    @Override
    public int getTeam() {
        return _team;
    }

    public void setPlayerId(int playerId){
        _playerId = playerId;
    }

    public int getPlayerId(){
        return _playerId;
    }

    public int getHealth() {
        return _health;
    }

    public void setHealth(int health) {
        this._health = health;
    }

    private void doDamage(long ms){
        for (GameObject other : _inCollision){
            if (other instanceof Damageable){
                if (((Damageable) other).getTeam() == _team) continue;
                ((Damageable) other).getHurt((int) (_dps * ms / 1000));
            }
        }
    }
}
