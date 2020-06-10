package Host;

import Common.GameObject;
import Common.MatchCommon;
import Common.PlayerTargetSkillHost;
import Common.UIManager;

public class HealthUpHost extends PlayerTargetSkillHost {
    public HealthUpHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster) {
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.getProperty().setHealth(player.getProperty().getHealth() + 10000);
    }
}
