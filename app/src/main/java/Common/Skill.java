package Common;

public interface Skill {
    String getName();
    void cast(GameObject caster);
    SkillTargetType getSkillTargetType();
}
