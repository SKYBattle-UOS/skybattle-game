package Common;

import com.example.Client.Core;
import com.example.Client.ImageType;

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

    @Override
    public void setPickedUp(boolean pickedUp) {
        super.setPickedUp(pickedUp);
        if (pickedUp){
            setRenderComponent(Core.get().getRenderer()
                    .createRenderComponent(this, ImageType.INVISIBLE));
        }
    }
}
