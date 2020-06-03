package Host;
import com.example.Client.GetYourHPClient;
import Common.GameObject;
import Common.PlayerProperty;

public class GetYourHPHost extends GetYourHPClient {
    public GetYourHPHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {

        WorldSetterHost wsh = CoreHost.get().getMatch().getWorldSetterHost();
        wsh.generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}
