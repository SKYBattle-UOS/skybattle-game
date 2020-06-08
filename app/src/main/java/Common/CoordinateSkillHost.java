package Common;

public abstract class CoordinateSkillHost extends CoordinateSkill {
    public CoordinateSkillHost(MatchCommon match) {
        super(match);
    }

    @Override
    public final String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void runCoolTime(int seconds, UIManager uiManager) {
        throw new UnsupportedOperationException();
    }
}
