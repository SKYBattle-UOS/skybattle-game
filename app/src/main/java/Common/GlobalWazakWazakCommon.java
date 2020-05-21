package Common;

public abstract class GlobalWazakWazakCommon implements Skill {
    protected double _lat;
    protected double _lon;

    @Override
    public String getName() {
        return "원격 와작와작 뻥!";
    }

    @Override
    public void cast(GameObject caster) {

    }

    @Override
    public SkillTargetType getSkillTargetType() {
        return SkillTargetType.COORDINATE;
    }

    @Override
    public void setTargetCoord(double lat, double lon) {
        _lat = lat;
        _lon = lon;
    }

    @Override
    public void setTargetPlayer(int playerId) {

    }
}
