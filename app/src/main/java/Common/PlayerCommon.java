package Common;

import android.util.Log;

import java.io.IOException;

public abstract class PlayerCommon extends GameObject implements Damageable {
    public static int playerIdDirtyFlag;
    public static int healthDirtyFlag;
    public static int teamDirtyFlag;
    public static int startFromHereFlag;

    {
        int i = GameObject.startFromHereFlag;
        i *= 2;
        playerIdDirtyFlag = i;
        i *= 2;
        healthDirtyFlag = i;
        i *= 2;
        teamDirtyFlag = i;
        i *= 2;
        startFromHereFlag = i;
    }

    protected Skill[] _skills = new Skill[4];
    protected int _shouldCast = 0;
    protected int _playerId;
    protected int _health = 100000;
    protected int _dps = 20000;
    protected int _team;

    protected PlayerCommon(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);

        try {
            if ((dirtyFlag & playerIdDirtyFlag) != 0)
                stream.write(_playerId, 32);

            if ((dirtyFlag & healthDirtyFlag) != 0)
                stream.write(_health, 32);

            if ((dirtyFlag & teamDirtyFlag) != 0)
                stream.write(_team, 1);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);

        if ((dirtyFlag & playerIdDirtyFlag) != 0)
            _playerId = stream.read(32);

        if ((dirtyFlag & healthDirtyFlag) != 0){
            _health = stream.read(32);
            Log.i("hehe", "health Up: " + _health);
        }

        if ((dirtyFlag & teamDirtyFlag) != 0)
            _team = stream.read(1);
    }

    public Skill[] getSkills() { return _skills; }

    public int getTeam() {
        return _team;
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

    @Override
    public void getHurt(int damage) {
        // 0 defense
        setHealth(getHealth() - damage);
    }
}
