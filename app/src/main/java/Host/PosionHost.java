package Host;

import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;

public class PosionHost extends PosionCommon {
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
