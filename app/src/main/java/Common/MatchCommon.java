package Common;

import com.example.Client.GameObjectRegistry;
import com.example.Client.Player;

import java.util.List;

import Host.WorldSetterHost;

public interface MatchCommon {
    LatLonByteConverter getConverter();
    Collider getCollider();
    GameObjectRegistry getRegistry();
    List<GameObject> getWorld();
    List<PlayerCommon> getPlayers();
    void setTimer(Runnable callback, float seconds);
}
