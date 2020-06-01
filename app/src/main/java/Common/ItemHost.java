package Common;

import Host.CoreHost;
import Host.HealthUpHost;
import Host.PickUpCondition;
import Host.PickUpTest;

public class ItemHost extends GameObject implements Pickable, Item {
    private ItemProperty _property = new ItemProperty();
    private PickUpCondition _pickUpCondition = new PickUpTest();

    protected ItemHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
        _property.setSkill(new HealthUpHost());
    }

    public static GameObject createInstance() {
        return new ItemHost(0, 0, "Item");
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);
        _property.writeToStream(stream, dirtyFlag);
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
    public boolean isPickedUp() {
        return _property.isPickedUp();
    }

    @Override
    public boolean pickUp(GameObject owner) {
        if (_property.isPickedUp()) return false;

        if (_pickUpCondition.evalulate(owner, this)){
            _property.setOwner(owner);
            _property.setPickedUp(true);
            CoreHost.get().getMatch().getWorldSetterHost()
                    .generateUpdateInstruction(getNetworkId(),
                            ItemProperty.ownerDirtyFlag | ItemProperty.isPickedUpDirtyFlag);
            return true;
        }

        return false;
    }

    @Override
    public GameObject getGameObject() {
        return this;
    }

    @Override
    public ItemProperty getProperty() {
        return _property;
    }
}
