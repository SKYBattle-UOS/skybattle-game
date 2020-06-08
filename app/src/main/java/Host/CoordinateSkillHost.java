package Host;

import Common.CoordinateSkill;

public abstract class CoordinateSkillHost extends CoordinateSkill {
    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void runCoolTime(int seconds) {
        throw new UnsupportedOperationException();
    }
}
