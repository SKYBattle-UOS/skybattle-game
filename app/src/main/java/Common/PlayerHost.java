package Common;

import java.util.Collection;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;
import Host.GlobalWazakWazakHost;
import Host.HealthUpCommon;
import Host.HealthUpHost;
import Host.WazakWazakHost;

public class PlayerHost extends PlayerCommon {
    private double[] _newPosTemp = new double[2];

    public PlayerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _skills[0] = new WazakWazakHost();
        _skills[1] = new GlobalWazakWazakHost();
        _skills[2] = new HealthUpHost();
        _skills[3] = new HealthUpHost();
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

        for (Skill skill : _skills)
            if (skill.getDirty())
                skill.cast(this);
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

    protected void networkUpdate(){
        int dirtyFlag = 0;

        ClientProxy client = CoreHost.getInstance().getNetworkManager().getClientById(getPlayerId());
        Queue<InputState> inputs = client.getUnprocessedInputs();
        while (true) {
            InputState input = inputs.poll();
            if (input == null) break;

            switch (input.qwer){
                case 0: case 1: case 2: case 3:
                    _skills[input.qwer].setDirty(true);
                    dirtyFlag |= PlayerHost.skillDirtyFlag;

                    // target is coordinate
                    if (input.lat * input.lon != 0){
                        _match.getConverter().restoreLatLon(input.lat, input.lon, _newPosTemp);
                        ((CoordinateSkill) _skills[input.qwer]).setTargetCoord(_newPosTemp[0], _newPosTemp[1]);
                    }
                    // target is player
                    else if (input.playerId >= 0){
                        ((PlayerTargetSkill) _skills[input.qwer]).setTargetPlayer(input.playerId);
                    }
                    break;

                // just new position
                case 4:
                    _match.getConverter().restoreLatLon(input.lat, input.lon, _newPosTemp);
                    setPosition(_newPosTemp[0], _newPosTemp[1]);
                    dirtyFlag |= PlayerHost.posDirtyFlag;
                    break;
            }
        }

        _match.getWorldSetterHost().generateUpdateInstruction(getNetworkId(), dirtyFlag);
    }
}
