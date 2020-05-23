package Host;

import Common.GameObject;
import Common.PlayerTargetSkill;

public abstract class HealthUpCommon extends PlayerTargetSkill {
    @Override
    public String getName() {
        return "회복";
    }

    @Override
    public void cast(GameObject caster) {

    }
}
