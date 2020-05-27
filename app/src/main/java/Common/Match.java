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
    List<PlayerCommon> getPlayers();
    GameObject createGameObject(int classId, boolean addToCollider);
    void setTimer(Runnable callback, float seconds);
}
