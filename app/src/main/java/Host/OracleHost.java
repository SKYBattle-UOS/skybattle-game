package Host;
import com.example.Client.OracleClient;
import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;

public class OracleHost extends OracleClient {
    public OracleHost(int index) {
        super(index);
    }

    public OracleHost() {
    }

    @Override
    public void cast(GameObject caster) {

        WorldSetterHost wsh = CoreHost.get().getMatch().getWorldSetterHost();
        wsh.generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}