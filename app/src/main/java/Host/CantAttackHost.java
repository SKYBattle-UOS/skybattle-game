package Host;
import Common.GameObject;
import Common.MatchCommon;
import Common.PlayerTargetSkillHost;

public class CantAttackHost extends PlayerTargetSkillHost {
    public CantAttackHost(MatchCommon match) {
        super(match);
    }

    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        DamageCalculator originalDC = player.getDamageCalculator();
        player.setDamageCalculator(new ZeroDamageCalculator());
        CoreHost.get().getMatch().setTimer(() -> player.setDamageCalculator(originalDC), 3f);
    }
}
