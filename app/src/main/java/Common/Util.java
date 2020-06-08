package Common;

import com.example.Client.BattleFieldClient;
import com.example.Client.GameObjectFactory;
import com.example.Client.PlayerClient;

import Host.BattleFieldHost;
import Host.DummyPlayerHost;
import Host.GlobalWazakWazakHost;
import Host.HealthUpHost;
import Host.SuicideHost;
import Host.SpiderMineHost;

public class Util {
    public static final int PORT = 9998;

    public static int PlayerClassId;
    public static int ItemClassId;
    public static int DummyPlayerClassId;
    public static int BattleFieldClassId;

    public static int SpiderMineClassId;
    public static int GlobalWazakWazakClassId;
    public static int HealthUpClassId;
    public static int SuicideClassId;

    public static void registerGameObjects(GameObjectFactory factory){
        PlayerClassId = factory.registerGameObject(PlayerClient::new);
        ItemClassId = factory.registerGameObject(ItemClient::new);
        DummyPlayerClassId = factory.registerGameObject(PlayerClient::new);
        BattleFieldClassId = factory.registerGameObject(BattleFieldClient::new);

        SpiderMineClassId = factory.registerSkill(SpiderMineClient::new);
        GlobalWazakWazakClassId = factory.registerSkill(GlobalWazakWazakCommon::new);
        HealthUpClassId = factory.registerSkill(HealthUpCommon::new);
        SuicideClassId = factory.registerSkill(SuicideClient::new);
    }

    public static void registerGameObjectsHost(GameObjectFactory factory){
        // should be same order as registerGameObjects !!!!!!!!!!
        factory.registerGameObject(PlayerHost::new);
        factory.registerGameObject(ItemHost::new);
        factory.registerGameObject(DummyPlayerHost::new);
        factory.registerGameObject(BattleFieldHost::new);

        factory.registerSkill(SpiderMineHost::new);
        factory.registerSkill(GlobalWazakWazakHost::new);
        factory.registerSkill(HealthUpHost::new);
        factory.registerSkill(SuicideHost::new);
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
