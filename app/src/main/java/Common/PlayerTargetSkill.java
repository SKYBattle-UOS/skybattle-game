package Common;

public abstract class PlayerTargetSkill implements Skill {
    protected int _networkId;

    @Override
    public SkillTargetType getSkillTargetType() {
        return SkillTargetType.PLAYER;
    }

    @Override
    public void setTargetPlayer(int networkId) {
        _networkId = networkId;
    }

    @Override
    public void setTargetCoord(double lat, double lon) {

    }
}
