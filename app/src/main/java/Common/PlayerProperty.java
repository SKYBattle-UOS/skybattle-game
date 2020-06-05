package Common;

import androidx.annotation.NonNull;

import com.example.Client.PlayerClient;
import com.example.Client.PlayerState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class PlayerProperty {
    public static int playerIdDirtyFlag;
    public static int healthDirtyFlag;
    public static int maxHealthDirtyFlag;
    public static int teamDirtyFlag;
    public static int skillDirtyFlag;
    public static int endOfFlag;
    public static int endOfFlagPos;
    public static int invincibilityFlag;
    public static int cantAttackFlag;
    public static int reflectAttackFlag;
    public static int playerStateFlag;

    static {
        int i = GameObject.EndOfFlagPos;
        playerIdDirtyFlag = 1 << i++;
        healthDirtyFlag = 1 << i++;
        teamDirtyFlag = 1 << i++;
        skillDirtyFlag = 1 << i++;
        maxHealthDirtyFlag = 1 << i++;
        invincibilityFlag = 1 << i++;
        cantAttackFlag = 1 << i++;
        reflectAttackFlag = 1 << i++;
        playerStateFlag = 1 << i++;
        endOfFlagPos = i;
        endOfFlag = 1 << i++;
    }

    private ArrayList<Skill> _skills = new ArrayList<>();
    private ReadOnlyList<Skill> _readOnlySkills = new ReadOnlyList<>(_skills);
    private int _playerId;
    private int _health = 100000;
    private int _maxHealth = 100000;
    private int _team;
    private int _dps = 20000;
    private boolean _isInvincible;
    private boolean _cantAttack;
    private boolean _reflectAttack;
    private PlayerState _playerState = PlayerState.NORMAL;
    private Player _player;

    public PlayerProperty(Player player) {
        _player = player;
        while (_skills.size() < 4) _skills.add(new InstantSkill() {
            @Override
            public String getName() {
                return "Skill";
            }

            @Override
            public void cast(GameObject caster) {

            }
        });
    }

    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        if ((dirtyFlag & playerIdDirtyFlag) != 0)
            setPlayerId(stream.read(32));

        if ((dirtyFlag & healthDirtyFlag) != 0)
            setHealth(stream.read(32));

        if ((dirtyFlag & maxHealthDirtyFlag) != 0)
            setMaxHealth(stream.read(32));

        if ((dirtyFlag & teamDirtyFlag) != 0)
            setTeam(stream.read(1));

        if ((dirtyFlag & invincibilityFlag) != 0)
            setInvincibility(stream.read(1) == 1);

        if ((dirtyFlag & cantAttackFlag) != 0)
            setCantAttack(stream.read(1) == 1);

        if ((dirtyFlag & reflectAttackFlag) != 0)
            setReflectAttack(stream.read(1) == 1);

        if ((dirtyFlag & skillDirtyFlag) != 0){
            for (Skill skill : _skills){
                skill.readFromStream(stream);
            }
        }

        if ((dirtyFlag & playerStateFlag) != 0){
            setPlayerState(PlayerState.values()[stream.read(4)]);
        }
    }

    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        try {
            if ((dirtyFlag & playerIdDirtyFlag) != 0)
                stream.write(getPlayerId(), 32);

            if ((dirtyFlag & healthDirtyFlag) != 0)
                stream.write(getHealth(), 32);

            if ((dirtyFlag & maxHealthDirtyFlag) != 0)
                stream.write(getMaxHealth(), 32);

            if ((dirtyFlag & teamDirtyFlag) != 0)
                stream.write(getTeam(), 1);

            if ((dirtyFlag & invincibilityFlag) != 0)
                stream.write(isInvincible() ? 1 : 0, 1);

            if ((dirtyFlag & cantAttackFlag) != 0)
                stream.write(getCantAttack() ? 1 : 0, 1);

            if ((dirtyFlag & reflectAttackFlag) != 0)
                stream.write(getReflectAttack() ? 1 : 0, 1);

            if ((dirtyFlag & skillDirtyFlag) != 0) {
                for (Skill skill : getSkills()){
                    skill.writeToStream(stream);
                }
            }

            if ((dirtyFlag & playerStateFlag) != 0){
                stream.write(_playerState.ordinal(), 4);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<Skill> getSkills(PlayerHost.Friend friend) {
        Objects.requireNonNull(friend);
        return _skills;
    }

    public List<Skill> getSkills(PlayerClient.Friend friend) {
        Objects.requireNonNull(friend);
        return _skills;
    }

    public List<Skill> getSkills(CharacterFactory.Friend friend) {
        Objects.requireNonNull(friend);
        return _skills;
    }

    public ReadOnlyList<Skill> getSkills(){
        return _readOnlySkills;
    }

    public int getTeam() {
        return _team;
    }

    public void setTeam(int team){
        _team = team;
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

    public boolean isInvincible() {
        return _isInvincible;
    }

    public void setInvincibility(boolean invincibility){
        _isInvincible = invincibility;
    }

    public boolean getCantAttack() {
        return _cantAttack;
    }

    public void setCantAttack(boolean cantAttack){
        _cantAttack = cantAttack;
    }

    public void setReflectAttack(boolean reflectAttack){
        _reflectAttack = reflectAttack;
    }

    public boolean getReflectAttack() {
        return _reflectAttack;
    }

    public int getMaxHealth() { return _maxHealth; }

    public void setMaxHealth(int health) { _maxHealth = health; }

    public int getDPS(){
        return _dps;
    }

    public void setDPS(int dps){ _dps = dps; }

    public void setPlayerState(PlayerState state){
        _playerState = state;
        _player.onPlayerStateChange(state);
    }

    public PlayerState getPlayerState() {
        return _playerState;
    }

    public void move(PlayerProperty other) {
        _skills = other._skills;
        _readOnlySkills = other._readOnlySkills;
        _team = other._team;
        _playerState = other._playerState;
        _playerId = other._playerId;
        _isInvincible = other._isInvincible;
        _health = other._health;
        _maxHealth = other._maxHealth;
        _cantAttack = other._cantAttack;
        _reflectAttack = other._reflectAttack;
        _dps = other._dps;
    }
}
