package Common;

import com.example.Client.GameObjectFactory;
import com.example.Client.PlayerClient;

import java.io.IOException;

import Host.DummyPlayerHost;

public class Util {
    public static final int PORT = 9998;

    public static int PlayerClassId;
    public static int ItemClassId;
    public static int DummyPlayerClassId;

    public static int WazakWazakClassId;
    public static int GlobalWazakWazakClassId;
    public static int HealthUpClassId;

    public static void registerGameObjects(GameObjectFactory factory){
        PlayerClassId = factory.registerGameObject(PlayerClient::new);
        ItemClassId = factory.registerGameObject(ItemClient::new);
        DummyPlayerClassId = factory.registerGameObject(PlayerClient::new);

        WazakWazakClassId = factory.registerSkill(WazakWazakCommon::new);
        GlobalWazakWazakClassId = factory.registerSkill(GlobalWazakWazakCommon::new);
        HealthUpClassId = factory.registerSkill(HealthUpCommon::new);
    }

    public static void registerGameObjectsHost(GameObjectFactory factory){
        // should be same order as registerGameObjects !!!!!!!!!!
        factory.registerGameObject(PlayerHost::new);
        factory.registerGameObject(ItemHost::new);
        factory.registerGameObject(DummyPlayerHost::new);
    }

    public static void sendHas(OutputBitStream outPacket, boolean has) {
        try {
            outPacket.write(has ? 1 : 0, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasMessage(InputBitStream packet) {
        return packet.read(1) == 1;
    }

    public static float distanceBetweenLatLon(double lat0, double lon0, double lat1, double lon1){
        float[] results = new float[3];
        android.location.Location.distanceBetween(lat0, lon0, lat1, lon1, results);
        return results[0];
    }
}
