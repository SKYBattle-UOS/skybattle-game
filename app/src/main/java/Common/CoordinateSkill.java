package Common;

public abstract class CoordinateSkill implements Skill {
    protected double _lat;
    protected double _lon;

    @Override
    public SkillTargetType getSkillTargetType() {
        return SkillTargetType.COORDINATE;
    }

    @Override
    public void setTargetPlayer(int playerId) {

    }

    @Override
    public void setTargetCoord(double lat, double lon) {
        _lat = lat;
        _lon = lon;
    }
}
