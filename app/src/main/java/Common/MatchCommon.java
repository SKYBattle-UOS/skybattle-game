package Common;

import com.example.Client.GameObjectRegistry;

import java.util.List;

public interface MatchCommon {
    LatLonByteConverter getConverter();
    Collider getCollider();
    GameObjectRegistry getRegistry();
    List<GameObject> getWorld();
    List<Player> getPlayers();
    void setTimer(Runnable callback, float seconds);
}
