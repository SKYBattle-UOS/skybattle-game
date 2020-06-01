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

    public static void registerGameObjects(GameObjectFactory factory){
        PlayerClassId = factory.registerCreateMethod(PlayerClient::createInstance);
        ItemClassId = factory.registerCreateMethod(ItemClient::createInstance);
        DummyPlayerClassId = factory.registerCreateMethod(PlayerClient::createInstance);
    }

    public static void registerGameObjectsHost(GameObjectFactory factory){
        // should be same order as registerGameObjects !!!!!!!!!!
        factory.registerCreateMethod(PlayerHost::createInstance);
        factory.registerCreateMethod(ItemHost::createInstance);
        factory.registerCreateMethod(DummyPlayerHost::createInstance);
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
