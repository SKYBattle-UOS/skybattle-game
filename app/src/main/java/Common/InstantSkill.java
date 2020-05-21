package Common;

public abstract class InstantSkill implements Skill {
    @Override
    public void setTargetCoord(double lat, double lon) {

    }

    @Override
    public SkillTargetType getSkillTargetType() {
        return SkillTargetType.INSTANT;
    }

    @Override
    public void setTargetPlayer(int playerId) {

    }
}
