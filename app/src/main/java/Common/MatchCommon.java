package Common;

import com.example.Client.GameObjectRegistry;

public interface MatchCommon {
    LatLonByteConverter getConverter();
    Collider getCollider();
    GameObjectRegistry getRegistry();
    ReadOnlyList<GameObject> getWorld();
    ReadOnlyList<Player> getPlayers();
    CharacterFactory getCharacterFactory();
    void setTimer(Object timerOwner, Runnable callback, float seconds);
    void killAllTimers(Object owner);
}
