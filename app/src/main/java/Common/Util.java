package Common;

import com.example.Client.GameObjectFactory;

import java.io.IOException;

public class Util {
    public static final int PORT = 9998;

    public static void registerGameObjects(GameObjectFactory factory){
        TempPlayer.classId = factory.registerCreateMethod(TempPlayer::createInstance);
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
}
