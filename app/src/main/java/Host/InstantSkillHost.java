package Host;

import Common.InstantSkill;

public abstract class InstantSkillHost extends InstantSkill {
    @Override
    public final String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void runCoolTime(int seconds) {
        throw new UnsupportedOperationException();
    }
}
