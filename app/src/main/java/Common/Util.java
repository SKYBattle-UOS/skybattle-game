package Common;

import com.example.Client.BattleFieldClient;
import com.example.Client.GameObjectFactory;
import com.example.Client.SupplyHealthClient;
import com.example.Client.HealthUpClient;
import com.example.Client.ItemClient;
import com.example.Client.PlayerClient;
import com.example.Client.SpiderMineClient;
import com.example.Client.SuicideClient;
import com.example.Client.ZombieSensorClient;
import com.example.Client.ZombieSightClient;

import Host.BattleFieldHost;
import Host.DummyPlayerHost;
import Host.SupplyHealthHost;
import Host.HealthUpHost;
import Host.PlayerHost;
import Host.SuicideHost;
import Host.SpiderMineHost;
import Host.ZombieSensorHost;
import Host.ZombieSightHost;

public class Util {
    public static final int PORT = 9998;

    public static int PlayerClassId;
    public static int ItemClassId;
    public static int DummyPlayerClassId;
    public static int BattleFieldClassId;

    public static int SpiderMineClassId;
    public static int SupplyHealthClassId;
    public static int HealthUpClassId;
    public static int SuicideClassId;
    public static int ZombieSensorId;
    public static int ZombieSightId;

    public static void registerGameObjects(GameObjectFactory factory, MatchCommon match, UIManager uiManager){
        PlayerClassId = factory.registerGameObject(PlayerClient::new);
        ItemClassId = factory.registerGameObject(ItemClient::new);
        DummyPlayerClassId = factory.registerGameObject(PlayerClient::new);
        BattleFieldClassId = factory.registerGameObject(BattleFieldClient::new);

        SpiderMineClassId = factory.registerSkill(() -> new SpiderMineClient(match, uiManager));
        SupplyHealthClassId = factory.registerSkill(() -> new SupplyHealthClient(match, uiManager));
        HealthUpClassId = factory.registerSkill(() -> new HealthUpClient(match, uiManager));
        SuicideClassId = factory.registerSkill(() -> new SuicideClient(match, uiManager));
        ZombieSensorId = factory.registerSkill(() -> new ZombieSensorClient(match, uiManager));
        ZombieSightId = factory.registerSkill(() -> new ZombieSightClient(match, uiManager));
    }

    public static void registerGameObjectsHost(GameObjectFactory factory, MatchCommon match){
        // should be same order as registerGameObjects !!!!!!!!!!
        factory.registerGameObject(PlayerHost::new);
        factory.registerGameObject(ItemHost::new);
        factory.registerGameObject(DummyPlayerHost::new);
        factory.registerGameObject(BattleFieldHost::new);

        factory.registerSkill(() -> new SpiderMineHost(match, null));
        factory.registerSkill(() -> new SupplyHealthHost(match, null));
        factory.registerSkill(() -> new HealthUpHost(match, null));
        factory.registerSkill(() -> new SuicideHost(match, null));
        factory.registerSkill(() -> new ZombieSensorHost(match, null));
        factory.registerSkill(() -> new ZombieSightHost(match, null));
    }

    public static void sendHas(OutputBitStream outPacket, boolean has) {
        outPacket.write(has ? 1 : 0, 1);
    }

    public static boolean hasMessage(InputBitStream packet) {
        return packet.read(1) == 1;
    }

    public static float distanceBetweenLatLon(double lat0, double lon0, double lat1, double lon1){
        float[] results = new float[3];
        android.location.Location.distanceBetween(lat0, lon0, lat1, lon1, results);
        return results[0];
    }

    public static float distanceBetweenLatLon(double[] latlon0, double[] latlon1){
        float[] results = new float[3];
        android.location.Location.distanceBetween(latlon0[0], latlon0[1], latlon1[0], latlon1[1], results);
        return results[0];
    }

    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public static Player findPlayerById(MatchCommon match, int playerId) {
        for (Player p : match.getPlayers())
            if (p.getProperty().getPlayerId() == playerId)
                return p;

        return null;
    }

    public static GameObject findGOByName(MatchCommon match, String name) {
        for (GameObject go : match.getWorld())
            if (go.getName().equals(name))
                return go;
        return null;
    }
}
