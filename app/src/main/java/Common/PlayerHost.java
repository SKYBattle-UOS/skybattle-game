package Common;

import java.util.Collection;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;
import Host.PlaceHolderSkill;
import Host.TempSkillHost;

public class PlayerHost extends PlayerCommon {
    private double[] _newPosTemp = new double[2];

    public PlayerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _skills[0] = new TempSkillHost();
        _skills[1] = new PlaceHolderSkill();
        _skills[2] = new PlaceHolderSkill();
        _skills[3] = new PlaceHolderSkill();
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
        // passive
        Collection<CollisionState> collisions = _match.getCollider().getCollisions(this);
        for (CollisionState collision : collisions){
            processColiision(collision, ms);
        }

        if ((_shouldCast & 1) != 0)
            _skills[0].cast(this);

        if ((_shouldCast & 2) != 0)
            _skills[1].cast(this);

        if ((_shouldCast & 4) != 0)
            _skills[2].cast(this);

        if ((_shouldCast & 8) != 0)
            _skills[3].cast(this);

        _shouldCast = 0;
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

            switch (input.qwer){
                // q
                case 0:
                    _shouldCast |= 1;
                    break;

                // w
                case 1:
                    _shouldCast |= 2;
                    break;

                // e
                case 2:
                    _shouldCast |= 4;
                    break;

                // r
                case 3:
                    _shouldCast |= 8;
                    break;

                // just new position
                case 4:
                    _match.getConverter().restoreLatLon(input.lat, input.lon, _newPosTemp);
                    setPosition(_newPosTemp[0], _newPosTemp[1]);
                    dirtyFlag |= 1;
                    break;
            }
        }

        _match.getWorldSetterHost().generateUpdateInstruction(getNetworkId(), dirtyFlag);
    }
}
