package Common;

import java.util.Collection;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;

public class PlayerHost extends PlayerCommon {
    private double[] _newPosTemp = new double[2];

    public PlayerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public static GameObject createInstance(){
        return new PlayerHost(0, 0, "Player");
    }

    @Override
    public void before(long ms) {
        networkUpdate();
    }

    @Override
    public void update(long ms) {
        Collection<CollisionState> collisions = _collider.getCollisions(this);
        for (CollisionState collision : collisions){
            processColiision(collision, ms);
        }
    }

    @Override
    public void after(long ms) {

    }

    private void processColiision(CollisionState state, long ms){
        if (state.other instanceof Damageable && !state.isExit){
            if (((Damageable) state.other).getTeam() != _team)
                ((Damageable) state.other).getHurt((int) (_dps * ms / 1000));
        }
    }

    private void networkUpdate(){
        int dirtyFlag = 0;

        ClientProxy client = CoreHost.getInstance().getNetworkManager().getClientById(getPlayerId());
        Queue<InputState> inputs = client.getUnprocessedInputs();
        while (true) {
            InputState input = inputs.poll();
            if (input == null) break;

            if (input.qwer == 4){
                _converter.restoreLatLon(input.lat, input.lon, _newPosTemp);
                setPosition(_newPosTemp[0], _newPosTemp[1]);
                dirtyFlag |= 1;
            }
        }

        _worldSetterHost.generateUpdateInstruction(getNetworkId(), dirtyFlag);
    }
}
