package Host;

import Common.GameObject;
import Common.Skill;
import Common.SkillTargetType;

public class PlaceHolderSkill implements Skill {
    @Override
    public String getName() {
        return "Place Holder";
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
