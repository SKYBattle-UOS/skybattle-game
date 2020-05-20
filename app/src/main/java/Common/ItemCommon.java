package Common;

public abstract class ItemCommon extends GameObject implements Pickable {
    protected ItemCommon(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }
}
