package Host;
import Common.GameObject;
import Common.MatchCommon;
import Common.InvincibilityCommon;
import Common.UIManager;

public class InvincibilityHost extends InvincibilityCommon {
    public InvincibilityHost(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public void cast(GameObject caster){
        todo(caster,true);
        CoreHost.get().getMatch().setTimer(this, () -> todo(caster,false), 10);
    }

    public void todo(GameObject caster, boolean value){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

//        player.getProperty().setInvincibility(value);
    }
}
