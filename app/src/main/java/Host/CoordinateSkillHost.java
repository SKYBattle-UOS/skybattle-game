package Host;

import Common.CoordinateSkill;

public abstract class CoordinateSkillHost extends CoordinateSkill {
    @Override
    public final String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void runCoolTime(int seconds) {
        throw new UnsupportedOperationException();
    }
}
