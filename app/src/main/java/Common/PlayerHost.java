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
import Host.ZeroDamageApplier;
import Host.ZeroDamageCalculator;

import static Common.PlayerProperty.*;

public class PlayerHost extends GameObject implements Damageable, Player {
    public static class Friend {
        private Friend(){}
    }
    private static final Friend friend = new Friend();
    private double[] _newPosTemp = new double[2];
    private ArrayList<Item> _itemsToRemove = new ArrayList<>();
    private DamageApplier _damageApplier = new ZeroDamageApplier();
    private DamageCalculator _damageCalculator = new ZeroDamageCalculator();

    private PlayerProperty _property = new PlayerProperty(this){
        @Override
        public void setHealth(int health) {
            health = checkHealth(health);
            super.setHealth(health);
        }
    };

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

        for (int i = 0; i < _property.getSkills().size(); i++) {
            Skill skill = _property.getSkills().get(i);
            if (skill.isDirty()) {
                skill.cast(this);
            }
        }

        for (Item item : getItems()){
            Skill skill = item.getProperty().getSkill();
            if (skill.isDirty()){
                skill.cast(this);
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
            CoreHost.get().getMatch().getWorldSetterHost()
                    .generateUpdateInstruction(getNetworkId(), itemsDirtyFlag);
            _itemsToRemove.clear();
        }
    }


    private void processCollision(CollisionState state, long ms){
        if (state.other instanceof Damageable && !state.isExit){
            if (((Damageable) state.other).getTeam() != _property.getTeam()){
                int damage = _damageCalculator.calculateDamage(this, ms);
                ((Damageable) state.other).takeDamage(this, damage);
                CoreHost.get().getMatch().getWorldSetterHost()
                        .generateUpdateInstruction(state.other.getNetworkId(), healthDirtyFlag);
                }
        }

        if (state.other instanceof Pickable){
            if (((Pickable) state.other).getPickedUpBy(this)){
                getItems().add((Item) state.other);
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
                    Skill skill;
                    if (skillIndex < 4)
                        skill = _property.getSkills().get(skillIndex);
                    else
                        skill = getItems().get(skillIndex - 4).getProperty().getSkill();

                    skill.setDirty(true);
                    dirtyFlag |= skillDirtyFlag;

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

        CoreHost.get().getMatch()
                .getWorldSetterHost().generateUpdateInstruction(getNetworkId(), dirtyFlag);
    }

    private int checkHealth(int health) {
        if (health > _property.getMaxHealth())
            health = _property.getMaxHealth();
        else if (health <= 0){
            makeGhost();
            health = 0;
        }

        return health;
    }

    @Override
    public void takeDamage(Player attacker, int damage) {
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

    private void makeGhost(){
        getProperty().setHealth(100000);
        setDamageApplier(new ZeroDamageApplier());
        setDamageCalculator(new ZeroDamageCalculator());
        getProperty().setPlayerState(PlayerState.GHOST);
        setLook(ImageType.INVISIBLE);

        int flag = healthDirtyFlag | playerStateFlag | imageTypeDirtyFlag;

        CoreHost.get().getMatch().getWorldSetterHost()
                .generateUpdateInstruction(getNetworkId(), flag);
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