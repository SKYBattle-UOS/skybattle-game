package Host;

import Common.PlayerTargetSkill;

public abstract class PlayerTargetSkillHost extends PlayerTargetSkill {
    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void runCoolTime(int seconds) {
        throw new UnsupportedOperationException();
    }
}
