package Host;

import Common.InstantSkill;

public abstract class InstantSkillHost extends InstantSkill {
    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void runCoolTime(int seconds) {
        throw new UnsupportedOperationException();
    }
}
