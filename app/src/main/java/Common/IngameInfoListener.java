package Common;

import com.example.Client.PlayerState;

public interface IngameInfoListener {
    void onPlayerStateChange(PlayerState state);
    void onItemsChange();
    void onHealthChange(int health);
}
