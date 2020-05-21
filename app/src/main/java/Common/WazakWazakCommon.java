package Common;

public abstract class WazakWazakCommon implements Skill {
    @Override
    public String getName() {
        return "와작와작 뻥!";
    }

    @Override
    public void cast(GameObject caster) {

    }

    @Override
    public SkillTargetType getSkillTargetType() {
        return SkillTargetType.INSTANT;
    }

    @Override
    public void setTargetCoord(double lat, double lon) {

    }

    @Override
    public void setTargetPlayer(int playerId) {

    }
}
