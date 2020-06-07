package Common;

import com.example.Client.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerProperty {
    public static int playerIdDirtyFlag;
    public static int healthDirtyFlag;
    public static int maxHealthDirtyFlag;
    public static int teamDirtyFlag;
    public static int skillDirtyFlag;
    public static int playerStateDirtyFlag;
    public static int endOfFlag;
    public static int endOfFlagPos;

    static {
        int i = GameObject.EndOfFlagPos;
        playerIdDirtyFlag = 1 << i++;
        healthDirtyFlag = 1 << i++;
        teamDirtyFlag = 1 << i++;
        skillDirtyFlag = 1 << i++;
        maxHealthDirtyFlag = 1 << i++;
        playerStateDirtyFlag = 1 << i++;
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
    private PlayerState _playerState = PlayerState.NORMAL;

    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        if ((dirtyFlag & playerIdDirtyFlag) != 0)
            setPlayerId(stream.read(32));

        if ((dirtyFlag & healthDirtyFlag) != 0)
            setHealth(stream.read(32));

        if ((dirtyFlag & maxHealthDirtyFlag) != 0)
            setMaxHealth(stream.read(32));

        if ((dirtyFlag & teamDirtyFlag) != 0)
            setTeam(stream.read(1));

        if ((dirtyFlag & skillDirtyFlag) != 0){
            for (Skill skill : _skills){
                skill.readFromStream(stream);
            }
        }

        if ((dirtyFlag & playerStateDirtyFlag) != 0){
            setPlayerState(PlayerState.values()[stream.read(4)]);
        }
    }

    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        if ((dirtyFlag & playerIdDirtyFlag) != 0)
            stream.write(getPlayerId(), 32);

        if ((dirtyFlag & healthDirtyFlag) != 0)
            stream.write(getHealth(), 32);

        if ((dirtyFlag & maxHealthDirtyFlag) != 0)
            stream.write(getMaxHealth(), 32);

        if ((dirtyFlag & teamDirtyFlag) != 0)
            stream.write(getTeam(), 1);

        if ((dirtyFlag & skillDirtyFlag) != 0) {
            for (Skill skill : getSkills()){
                skill.writeToStream(stream);
            }
        }

        if ((dirtyFlag & playerStateDirtyFlag) != 0){
            stream.write(_playerState.ordinal(), 4);
        }

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

    public int getMaxHealth() { return _maxHealth; }

    public void setMaxHealth(int health) { _maxHealth = health; }

    public int getDPS(){
        return _dps;
    }

    public void setDPS(int dps){ _dps = dps; }

    public void setPlayerState(PlayerState state){
        _playerState = state;
    }

    public PlayerState getPlayerState() {
        return _playerState;
    }

    public void getFromFactory(PlayerProperty fromFactory) {
        _skills = fromFactory._skills;
        _readOnlySkills = fromFactory._readOnlySkills;
        _playerState = fromFactory._playerState;
        _health = fromFactory._health;
        _maxHealth = fromFactory._maxHealth;
        _dps = fromFactory._dps;
    }
}
