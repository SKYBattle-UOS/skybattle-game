package Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PlayerCommon extends GameObject implements Damageable {
    public static int playerIdDirtyFlag;
    public static int healthDirtyFlag;
    public static int maxHealthDirtyFlag;
    public static int teamDirtyFlag;
    public static int skillDirtyFlag;
    public static int startFromHereFlag;

    static {
        int i = GameObject.startFromHereFlag;
        playerIdDirtyFlag = i;
        i *= 2;
        healthDirtyFlag = i;
        i *= 2;
        teamDirtyFlag = i;
        i *= 2;
        skillDirtyFlag = i;
        i *= 2;
        maxHealthDirtyFlag = i;
        i *= 2;
        startFromHereFlag = i;
    }

    protected ArrayList<Skill> _skills = new ArrayList<>();
    protected int _playerId;
    protected int _health = 100000;
    protected int _maxHealth = 100000;
    protected int _dps = 20000;
    protected int _team;

    protected PlayerCommon(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        while (_skills.size() < 4) _skills.add(null);
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);

        try {
            if ((dirtyFlag & playerIdDirtyFlag) != 0)
                stream.write(getPlayerId(), 32);

            if ((dirtyFlag & healthDirtyFlag) != 0)
                stream.write(getHealth(), 32);

            if ((dirtyFlag & maxHealthDirtyFlag) != 0)
                stream.write(getMaxHealth(), 32);

            if ((dirtyFlag & teamDirtyFlag) != 0)
                stream.write(getTeam(), 1);

            if ((dirtyFlag & skillDirtyFlag) != 0) {
                for (Skill skill : _skills){
                    skill.writeToStream(stream);
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);

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
    }

    public List<Skill> getSkills() { return _skills; }

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

    @Override
    public void getHurt(int damage) {
        // 0 defense
        setHealth(getHealth() - damage);
    }
}
