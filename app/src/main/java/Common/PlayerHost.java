package Common;

import com.example.Client.ImageType;

import java.util.Collection;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;
import Host.GlobalWazakWazakHost;
import Host.HealthUpHost;
import Host.MatchHost;
import Host.WazakWazakHost;

public class PlayerHost extends PlayerCommon {
    private double[] _newPosTemp = new double[2];

    public PlayerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _skills.set(0, new WazakWazakHost());
        _skills.set(1, new GlobalWazakWazakHost());
        _skills.set(2, new HealthUpHost());
        _skills.set(3, new HealthUpHost());
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
            processCollision(collision, ms);
        }

        for (Skill skill : _skills)
            if (skill.isDirty())
                skill.cast(this);
    }

    @Override
    public void after(long ms) {
    }

    private void processCollision(CollisionState state, long ms){
        if (state.other instanceof Damageable && !state.isExit){
            if (((Damageable) state.other).getTeam() != _team){
                ((Damageable) state.other).getHurt((int) (_dps * ms / 1000));
                CoreHost.get().getMatch().getWorldSetterHost()
                        .generateUpdateInstruction(state.other.getNetworkId(), healthDirtyFlag);
            }
        }
    }

    protected void networkUpdate(){
        int dirtyFlag = 0;

        ClientProxy client = CoreHost.get().getNetworkManager().getClientById(getPlayerId());
        Queue<InputState> inputs = client.getUnprocessedInputs();
        while (true) {
            InputState input = inputs.poll();
            if (input == null) break;

            switch (input.qwer){
                // just new position
                case 0:
                    _match.getConverter().restoreLatLon(input.lat, input.lon, _newPosTemp);
                    setPosition(_newPosTemp[0], _newPosTemp[1]);
                    dirtyFlag |= PlayerHost.posDirtyFlag;
                    break;

                default:
                    int skillIndex = input.qwer - 1;
                    _skills.get(skillIndex).setDirty(true);
                    dirtyFlag |= PlayerHost.skillDirtyFlag;

                    // target is coordinate
                    if (input.lat * input.lon != 0){
                        _match.getConverter().restoreLatLon(input.lat, input.lon, _newPosTemp);
                        ((CoordinateSkill) _skills.get(skillIndex)).setTargetCoord(_newPosTemp[0], _newPosTemp[1]);
                    }
                    // target is player
                    else if (input.playerId >= 0){
                        ((PlayerTargetSkill) _skills.get(skillIndex)).setTargetPlayer(input.playerId);
                    }
                    break;
            }
        }

        CoreHost.get().getMatch()
                .getWorldSetterHost().generateUpdateInstruction(getNetworkId(), dirtyFlag);
    }

    @Override
    public void faceDeath() {
        PlayerHost deadPlayer = (PlayerHost) CoreHost.get().getMatch()
                .createGameObject(Util.PlayerClassId, true);

        deadPlayer.setPlayerId(getPlayerId());
        deadPlayer.setName("현재위치");
        deadPlayer.setLook(ImageType.INVISIBLE);
        deadPlayer.setPosition(getPosition()[0], getPosition()[1]);
    }

    @Override
    public void setHealth(int health) {
        if (health > _maxHealth)
            health = _maxHealth;
        else if (health < 0){
            scheduleDeath();
            health = 0;
        }

        super.setHealth(health);
    }

    @Override
    public void addItem(ItemCommon item) {
        super.addItem(item);
        _skills.add(item.getSkill());
    }
}
