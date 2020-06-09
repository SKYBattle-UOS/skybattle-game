package Host;
import Common.GameObject;
import Common.MatchCommon;
import Common.InvincibilityCommon;

public class InvincibilityHost extends InvincibilityCommon {
    public InvincibilityHost(MatchCommon match) {
        super(match);
    }

    @Override
    public void cast(GameObject caster){
        todo(caster,true);
        CoreHost.get().getMatch().setTimer(() -> todo(caster,false),10);
    }

    public void todo(GameObject caster, boolean value){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

//        player.getProperty().setInvincibility(value);
    }
}
