package Common;

public abstract class PlayerTargetSkill implements Skill {
    protected int _playerId;

    @Override
    public SkillTargetType getSkillTargetType() {
        return SkillTargetType.PLAYER;
    }

    @Override
    public void setTargetPlayer(int playerId) {
        _playerId = playerId;
    }
}
