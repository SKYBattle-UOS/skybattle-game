package Host;
import com.example.Client.GetDistanceClient;
import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;
import Common.PlayerTargetSkill;
import Common.Util;

public class GetDistanceHost extends GetDistanceClient {
    public GetDistanceHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {

        WorldSetterHost wsh = CoreHost.get().getMatch().getWorldSetterHost();
        wsh.generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}
