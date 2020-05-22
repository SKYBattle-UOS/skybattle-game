package Host;

import Common.GameObject;
import Common.PlayerHost;

public class DummyPlayerHost extends PlayerHost {
    public DummyPlayerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public static GameObject createInstance(){
        return new DummyPlayerHost(0, 0, "DummyPlayer");
    }

    @Override
    protected void networkUpdate() {
        // nothing
    }
}
