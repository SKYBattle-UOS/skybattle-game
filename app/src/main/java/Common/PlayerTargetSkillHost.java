package Common;

public abstract class PlayerTargetSkillHost extends PlayerTargetSkill {
    @Override
    public final String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void runCoolTime(int seconds) {
        throw new UnsupportedOperationException();
    }
}
