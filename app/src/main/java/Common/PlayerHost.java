package Common;

import androidx.annotation.NonNull;

import com.example.Client.PlayerState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;
import Host.DamageApplier;
import Host.DamageCalculator;
import Host.GameObjectHost;
import Host.ZeroDamageApplier;
import Host.ZeroDamageCalculator;

import static Common.PlayerProperty.*;

public class PlayerHost extends GameObjectHost implements Damageable, Player {
    private double[] _newPosTemp = new double[2];
    private ArrayList<Item> _itemsToRemove = new ArrayList<>();
    private DamageApplier _damageApplier = new ZeroDamageApplier();
    private DamageCalculator _damageCalculator = new ZeroDamageCalculator();

    private PlayerProperty _property = new PlayerProperty(){
        @Override
        public void setHealth(int health) {
            health = checkHealth(health);
            super.setHealth(health);
            getHeader().dirtyFlag |= healthDirtyFlag;
        }

        @Override
        public void setMaxHealth(int health) {
            super.setMaxHealth(health);
            getHeader().dirtyFlag |= maxHealthDirtyFlag;
        }

        @Override
        public void setPlayerId(int playerId) {
            super.setPlayerId(playerId);
            getHeader().dirtyFlag |= playerIdDirtyFlag;
        }

        @Override
        public void setPlayerState(PlayerState state) {
            super.setPlayerState(state);
            getHeader().dirtyFlag |= playerStateDirtyFlag;
        }

        @Override
        public void setTeam(int team) {
            super.setTeam(team);
            getHeader().dirtyFlag |= teamDirtyFlag;
        }
    };

    private boolean _shouldMakeZombie;
    private boolean _isInsideBattleField;

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);
        _property.writeToStream(stream, dirtyFlag);
    }

    @Override
    public void before(long ms) {
    }

    @Override
    public void update(long ms) {
        networkUpdate();

        // passive
        Collection<CollisionState> collisions = _match.getCollider().getCollisions(this);
        for (CollisionState collision : collisions){
            if (getProperty().getPlayerState() != PlayerState.GHOST)
                processCollision(collision, ms);
        }

        for (Skill skill : _property.getSkills()) {
            if (skill.isDirty()) {
                skill.cast(this);
                getHeader().dirtyFlag |= skillDirtyFlag;
            }
        }

        for (Item item : getItems()){
            Skill skill = item.getProperty().getSkill();
            if (skill.isDirty()){
                skill.cast(this);
                getHeader().dirtyFlag |= skillDirtyFlag;
                _itemsToRemove.add(item);
            }
        }
    }

    @Override
    public void after(long ms) {
        for (Item item : _itemsToRemove){
            getItems().remove(item);
        }
        if (!_itemsToRemove.isEmpty()){
            getHeader().dirtyFlag |= itemsDirtyFlag;
            _itemsToRemove.clear();
        }

        if (_shouldMakeZombie){
            makeZombie();
            _shouldMakeZombie = false;
        }
    }


    private void processCollision(CollisionState state, long ms){
        if (state.other instanceof Damageable){
            if (((Damageable) state.other).getTeam() != _property.getTeam()){
                int damage = _damageCalculator.calculateDamage(this, ms);
                ((Damageable) state.other).takeDamage(this, damage);
                }
        }

        if (state.other instanceof Pickable){
            if (((Pickable) state.other).getPickedUpBy(this)){
                getItems().add((Item) state.other);
                getHeader().dirtyFlag |= itemsDirtyFlag;
            }
        }
    }

    protected void networkUpdate(){
        ClientProxy client = CoreHost.get().getNetworkManager()
                .getClientById(_property.getPlayerId());
        Queue<InputState> inputs = client.getUnprocessedInputs();
        while (true) {
            InputState input = inputs.poll();
            if (input == null) break;

            switch (input.command){
                // just new position
                case 0:
                    _match.getConverter().restoreLatLon(input.lat, input.lon, _newPosTemp);
                    setPosition(_newPosTemp[0], _newPosTemp[1]);
                    break;

                default:
                    int skillIndex = input.command - 1;
                    Skill skill;
                    if (skillIndex < 4)
                        skill = _property.getSkills().get(skillIndex);
                    else
                        skill = getItems().get(skillIndex - 4).getProperty().getSkill();

                    skill.setDirty(true);

                    // target is coordinate
                    if (input.lat * input.lon != 0){
                        _match.getConverter().restoreLatLon(input.lat, input.lon, _newPosTemp);
                        ((CoordinateSkill) skill).setTargetCoord(_newPosTemp[0], _newPosTemp[1]);
                    }
                    // target is player
                    else if (input.playerId >= 0){
                        ((PlayerTargetSkill) skill).setTargetPlayer(input.playerId);
                    }
                    break;
            }
        }
    }

    private int checkHealth(int health) {
        if (health > _property.getMaxHealth())
            health = _property.getMaxHealth();
        else if (health <= 0){
            if (getProperty().getPlayerState() == PlayerState.NORMAL){
                _shouldMakeZombie = true;
                health = 10;
            }
            else if (getProperty().getPlayerState() == PlayerState.ZOMBIE)
                health = makeGhost();
        }

        return health;
    }

    public int makeZombie() {
        _match.getCharacterFactory().setCharacterProperty(this, 1);
        getProperty().setTeam(1);
        getProperty().setPlayerState(PlayerState.ZOMBIE);
        setName(getName() + " (좀비)");
        return getProperty().getHealth();
    }

    @Override
    public void takeDamage(GameObject attacker, int damage) {
        _damageApplier.applyDamage(this, attacker, damage);
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

    @Override
    public void setProperty(PlayerProperty property) {
        _property.getFromFactory(property);
    }

    private int makeGhost(){
        getProperty().setHealth(100000);
        setDamageApplier(new ZeroDamageApplier());
        setDamageCalculator(new ZeroDamageCalculator());
        getProperty().setPlayerState(PlayerState.GHOST);
        setLook(ImageType.INVISIBLE);
        return 100000;
    }

    public void setDamageApplier(@NonNull DamageApplier applier){
        _damageApplier = applier;
    }

    public DamageApplier getDamageApplier() {
        return _damageApplier;
    }

    public DamageCalculator getDamageCalculator(){
        return _damageCalculator;
    }

    public void setDamageCalculator(@NonNull DamageCalculator calculator){
        _damageCalculator = calculator;
    }
}