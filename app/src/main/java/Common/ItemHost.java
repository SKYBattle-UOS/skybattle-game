package Common;

public class ItemHost extends ItemCommon {
    protected ItemHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public static GameObject createInstance() {
        return new ItemHost(0, 0, "TempItem");
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
