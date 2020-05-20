package Common;

import com.example.Client.Core;
import com.example.Client.ImageType;

public class Item extends ItemCommon {
    protected Item(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        setRenderComponent(Core.getInstance().getRenderer().createRenderComponent(this, ImageType.FILLED_CIRCLE));
    }

    public static GameObject createInstance() {
        return new Item(0, 0, "Item");
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);

        if ((dirtyFlag & 4) != 0){
            _owner = _registry.getGameObject(stream.read(32));
        }
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
