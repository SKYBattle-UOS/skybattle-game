package Common;

import com.example.Client.GameObjectFactory;

import java.io.IOException;

import Host.TempPlayerHost;

public class Util {
    public static final int PORT = 9998;

    public static void registerGameObjects(GameObjectFactory factory){
        TempPlayer.classId = factory.registerCreateMethod(TempPlayer::createInstance);
    }

    public static void registerGameObjectsHost(GameObjectFactory factory){
        TempPlayerHost.classId = factory.registerCreateMethod(TempPlayerHost::createInstance);
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
