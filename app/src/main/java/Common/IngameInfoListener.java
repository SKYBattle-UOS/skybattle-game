package Common;

import com.example.Client.PlayerState;

public interface IngameInfoListener {
    default void onPlayerStateChange(PlayerState state){}
    default void onItemsChange(){}
    default void onHealthChange(int health){}
}
