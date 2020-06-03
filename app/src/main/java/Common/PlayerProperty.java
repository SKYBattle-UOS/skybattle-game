package Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    static {
        int i = GameObject.EndOfFlagPos;
        playerIdDirtyFlag = 1 << i++;
        healthDirtyFlag = 1 << i++;
        teamDirtyFlag = 1 << i++;
        skillDirtyFlag = 1 << i++;
        maxHealthDirtyFlag = 1 << i++;
        invincibilityFlag = 1 << i++;
        cantAttackFlag = 1 << i++;
        reflectAttackFlag= 1 << i++;
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

    public PlayerProperty() {
        while (_skills.size() < 4) _skills.add(null);
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

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<Skill> getSkills(Player.Friend friend) {
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
}
