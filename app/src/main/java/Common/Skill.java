package Common;

public interface Skill {
    String getName();
    SkillTargetType getSkillTargetType();
    void setTargetCoord(double lat, double lon);
    void setTargetPlayer(int playerId);
    void cast(GameObject caster);
}
