package Common;

public abstract class PlayerCommon extends GameObject implements Damageable {
    protected Skill[] _skills = new Skill[4];
    protected int _shouldCast = 0;
    protected int _playerId;
    protected int _health = 100000;
    protected int _dps = 20000;
    protected int _team;

    protected PlayerCommon(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
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
