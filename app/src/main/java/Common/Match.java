package Common;

import com.example.Client.GameObjectRegistry;

import java.util.List;

import Host.WorldSetterHost;

public interface Match {
    LatLonByteConverter getConverter();
    Collider getCollider();
    WorldSetterHost getWorldSetterHost();
    GameObjectRegistry getRegistry();
    List<GameObject> getWorld();
    GameObject createGameObject(int classId);
}
