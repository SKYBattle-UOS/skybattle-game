package Host;

import Common.GameObject;
import Common.MatchCommon;
import Common.PlayerTargetSkillHost;

public class PoisonHost extends PlayerTargetSkillHost {
    public PoisonHost(MatchCommon match) {
        super(match);
    }

    @Override
    public void cast(GameObject caster) {
        for (int i= 0; i < 10; i++)
        {
            CoreHost.get().getMatch().setTimer(this::todo, 1);
        }
    }

    public void todo(){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.getProperty().setHealth(player.getProperty().getHealth() - 5000);
    }
}
