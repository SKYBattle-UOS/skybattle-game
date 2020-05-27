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
        PlayerHost player = (PlayerHost) CoreHost.getInstance()
                .getMatch().getRegistry().getGameObject(_networkId);

        int nowHealth = player.getHealth();

        for(int i=0; i <30; i++){ //갱신에 따라 수정해야함
            player = (PlayerHost) CoreHost.getInstance()
                    .getMatch().getRegistry().getGameObject(_networkId);

            player.setHealth(nowHealth);
            CoreHost.getInstance()
                    .getMatch()
                    .getWorldSetterHost()
                    .generateUpdateInstruction(player.getNetworkId(), PlayerHost.healthDirtyFlag);

            player.getMatch().getWorldSetterHost().generateUpdateInstruction(caster.getNetworkId(), PlayerHost.skillDirtyFlag);
        }
    }
}
