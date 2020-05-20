package Host;

import Common.GameObject;
import Common.PlayerHost;

public class TempPlayerHost extends PlayerHost {
    public TempPlayerHost(float latitude, float longitude, String name){
        super(latitude, longitude, name);
    }

    public static GameObject createInstance(){
        return new TempPlayerHost(0, 0, "TempPlayer");
    }

    @Override
    public void update(long ms) {
    }
}
