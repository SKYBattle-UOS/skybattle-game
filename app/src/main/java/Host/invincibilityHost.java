package Host;
import com.example.Client.ImageType;
import Common.GameObject;
import Common.PlayerHost;
import Common.Util;
import Common.invincibilityCommon;

public class invincibilityHost extends invincibilityCommon {
    public invincibilityHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster){
        todo(0);
        CoreHost.get().getMatch().setTimer(() -> todo(0),1);
    }

    public void todo(int value){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.setInvincibility(value);
        CoreHost.get()
                .getMatch()
                .getWorldSetterHost()
                .generateUpdateInstruction(player.getNetworkId(), PlayerHost.invincibilityFlag);

        CoreHost.get().getMatch().getWorldSetterHost().generateUpdateInstruction(player.getNetworkId(), PlayerHost.invincibilityFlag);
    }
}
