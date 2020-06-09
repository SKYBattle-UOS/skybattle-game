package Common;

public abstract class PlayerTargetSkillHost extends PlayerTargetSkill {
    public PlayerTargetSkillHost(MatchCommon match) {
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
