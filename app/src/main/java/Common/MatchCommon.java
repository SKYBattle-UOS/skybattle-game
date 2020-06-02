package Common;

import com.example.Client.GameObjectRegistry;

public interface MatchCommon {
    LatLonByteConverter getConverter();
    Collider getCollider();
    GameObjectRegistry getRegistry();
    ReadOnlyList<GameObject> getWorld();
    ReadOnlyList<Player> getPlayers();
    void setTimer(Runnable callback, float seconds);
}
