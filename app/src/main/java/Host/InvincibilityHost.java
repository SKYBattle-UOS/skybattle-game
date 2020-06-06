package Host;
import Common.GameObject;
import Common.PlayerHost;
import Common.InvincibilityCommon;
import Common.PlayerProperty;

public class InvincibilityHost extends InvincibilityCommon {
    @Override
    public void cast(GameObject caster){
        todo(caster,true);
        CoreHost.get().getMatch().setTimer(() -> todo(caster,false),10);
    }

    public void todo(GameObject caster, boolean value){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

//        player.getProperty().setInvincibility(value);

        WorldSetterHost wsh = CoreHost.get().getMatch().getWorldSetterHost();
        CoreHost.get().getMatch().getWorldSetterHost().generateUpdateInstruction(player.getNetworkId(), player.getProperty().invincibilityFlag);
        wsh.generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}
