package Common;

import Host.CoreHost;
import Host.PickUpCondition;
import Host.PickUpNever;

public class ItemHost extends ItemCommon {
    private PickUpCondition _pickUpCondition = new PickUpNever();

    protected ItemHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public static GameObject createInstance() {
        return new ItemHost(0, 0, "Item");
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
    public void pickUp(GameObject owner) {
        if (_pickUpCondition.evalulate(owner)){
            _owner = owner;
            _isPickedUp = true;
            CoreHost.get().getMatch().getWorldSetterHost()
                    .generateUpdateInstruction(getNetworkId(), ownerDirtyFlag | isPickedUpDirtyFlag);
        }
    }
}
