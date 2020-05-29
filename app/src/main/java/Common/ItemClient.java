package Common;

import com.example.Client.GameObjectClient;

public class ItemClient extends GameObjectClient implements Item {
    ItemProperty _property = new ItemProperty(){
        @Override
        public void setPickedUp(boolean pickedUp) {
            super.setPickedUp(pickedUp);
            if (pickedUp)
                setLook(ImageType.INVISIBLE);
        }
    };

    protected ItemClient(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _property.setSkill(new HealthUpCommon());
    }

    public static GameObject createInstance() {
        return new ItemClient(0, 0, "Item");
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
    public GameObject getGameObject() {
        return this;
    }

    @Override
    public ItemProperty getProperty(){
        return _property;
    }
}
