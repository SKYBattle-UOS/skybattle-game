package Common;

import com.example.Client.Core;
import com.example.Client.ImageType;

import Host.HealthUpCommon;

public class Item extends ItemCommon {
    protected Item(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _skill = new HealthUpCommon();
    }

    public static GameObject createInstance() {
        return new Item(0, 0, "Item");
    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
    }

    @Override
    public void after(long ms) {

    }
}
