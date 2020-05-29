package Common;

import java.util.Collection;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;
import Host.GlobalWazakWazakHost;
import Host.HealthUpHost;
import Host.WazakWazakHost;

import static Common.PlayerProperty.*;

public class PlayerHost extends GameObject implements Damageable, Player{
    private double[] _newPosTemp = new double[2];

    private PlayerProperty _property = new PlayerProperty(){
        @Override
        public void setHealth(int health) {
            health = checkHealth(health);
            super.setHealth(health);
        }
    };

    public PlayerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _property.getSkills().set(0, new WazakWazakHost());
        _property.getSkills().set(1, new GlobalWazakWazakHost());
        _property.getSkills().set(2, new HealthUpHost());
        _property.getSkills().set(3, new HealthUpHost());
    }

    public static GameObject createInstance(){
        return new PlayerHost(0, 0, "Player");
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);
        _property.writeToStream(stream, dirtyFlag);
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

        for (Skill skill : _property.getSkills())
            if (skill.isDirty())
                skill.cast(this);
    }

    @Override
    public void after(long ms) {
    }

    private void processCollision(CollisionState state, long ms){
        if (state.other instanceof Damageable && !state.isExit){
            if (((Damageable) state.other).getTeam() != _property.getTeam()){
                ((Damageable) state.other).getHurt((int) (_property.getDPS() * ms / 1000));
                CoreHost.get().getMatch().getWorldSetterHost()
                        .generateUpdateInstruction(state.other.getNetworkId(), healthDirtyFlag);
            }
        }

        if (state.other instanceof Pickable){
            if (((Pickable) state.other).pickUp(this)){
                addItem((Item) state.other);
                CoreHost.get().getMatch().getWorldSetterHost()
                        .generateUpdateInstruction(getNetworkId(), itemsDirtyFlag);
            }
        }
    }

    protected void networkUpdate(){
        int dirtyFlag = 0;

        ClientProxy client = CoreHost.get().getNetworkManager().getClientById(_property.getPlayerId());
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
                    _property.getSkills().get(skillIndex).setDirty(true);
                    dirtyFlag |= skillDirtyFlag;

                    // target is coordinate
                    if (input.lat * input.lon != 0){
                        _match.getConverter().restoreLatLon(input.lat, input.lon, _newPosTemp);
                        ((CoordinateSkill) _property.getSkills().get(skillIndex))
                                .setTargetCoord(_newPosTemp[0], _newPosTemp[1]);
                    }
                    // target is player
                    else if (input.playerId >= 0){
                        ((PlayerTargetSkill) _property.getSkills().get(skillIndex))
                                .setTargetPlayer(input.playerId);
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

        PlayerProperty deadPlayerProperty = deadPlayer.getProperty();
        deadPlayerProperty.setPlayerId(_property.getPlayerId());
        deadPlayer.setName("현재위치");
        deadPlayer.setLook(ImageType.INVISIBLE);
        deadPlayer.setPosition(getPosition()[0], getPosition()[1]);
    }

    private int checkHealth(int health) {
        if (health > _property.getMaxHealth())
            health = _property.getMaxHealth();
        else if (health < 0){
            scheduleDeath();
            health = 0;
        }

        return health;
    }

    @Override
    public void addItem(Item item) {
        super.addItem(item);
        _property.getSkills().add(item.getProperty().getSkill());
    }

    @Override
    public void getHurt(int damage) {
        // 0 defense
        _property.setHealth(_property.getHealth() - damage);
    }

    @Override
    public int getTeam() {
        return _property.getTeam();
    }

    @Override
    public GameObject getGameObject() {
        return this;
    }

    @Override
    public PlayerProperty getProperty(){
        return _property;
    }
}
