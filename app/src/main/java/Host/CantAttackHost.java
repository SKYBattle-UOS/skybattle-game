package Host;
import Common.GameObject;
import Common.MatchCommon;
import Common.PlayerTargetSkillHost;
import Common.UIManager;

public class CantAttackHost extends PlayerTargetSkillHost {
    public CantAttackHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        DamageCalculator originalDC = player.getDamageCalculator();
        player.setDamageCalculator(new ZeroDamageCalculator());
        CoreHost.get().getMatch().setTimer(this, () -> player.setDamageCalculator(originalDC), 3f);
    }
}
