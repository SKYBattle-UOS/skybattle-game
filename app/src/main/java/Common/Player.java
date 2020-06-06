package Common;

import com.example.Client.PlayerState;

public interface Player {
    GameObject getGameObject();
    PlayerProperty getProperty();
    void setProperty(PlayerProperty property);
    default void onPlayerStateChange(PlayerState state){}
}
