package Common;

import com.example.Client.GameObjectFactory;

public class Settings {
    public static final int PORT = 9998;

    public static void registerGameObjects(GameObjectFactory factory){
        TempPlayer.classId = factory.registerCreateMethod(TempPlayer::createInstance);
    }
}
