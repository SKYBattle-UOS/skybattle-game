package Common;

public abstract class PlayerTargetSkillHost extends PlayerTargetSkill {
    public PlayerTargetSkillHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public final String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void runCoolTime(int seconds) {
        throw new UnsupportedOperationException();
    }
}
