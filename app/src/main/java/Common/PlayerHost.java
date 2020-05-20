package Common;

import android.util.Log;

import java.util.Collection;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;
import Host.GameObjectHost;

public class PlayerHost extends GameObjectHost implements Damageable {
    private int _playerId;
    private int _health = 100000;
    private int _dps = 20000;
    private int _team;

    public PlayerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
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
    public void before(long ms) {
        networkUpdate();
    }

    @Override
    public void update(long ms) {
        doDamage(ms);
    }

    @Override
    public void after(long ms) {

    }

    private void networkUpdate(){
        int dirtyFlag = 0;

        ClientProxy client = CoreHost.getInstance().getNetworkManager().getClientById(getPlayerId());
        Queue<InputState> inputs = client.getUnprocessedInputs();
        while (true) {
            InputState input = inputs.poll();
            if (input == null) break;

            double[] prevPos = getPosition();
            if (prevPos[0] != input.lat || prevPos[1] != input.lon) {
                setPosition(input.lat, input.lon);
                dirtyFlag |= 1;
            }
        }

        _worldSetterHost.generateUpdateInstruction(getNetworkId(), dirtyFlag);
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
        Collection<CollisionState> collisions = _collider.getCollisions();
        for (CollisionState collision : collisions){
            if (collision.other instanceof Damageable && !collision.isExit){
                if (((Damageable) collision.other).getTeam() == _team) continue;
                ((Damageable) collision.other).getHurt((int) (_dps * ms / 1000));
                Log.i("Stub", "collided");
            }
        }
    }
}
