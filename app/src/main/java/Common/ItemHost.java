package Common;

import Host.GameObjectHost;
import Host.HealthUpHost;
import Host.PickUpCondition;
import Host.PickUpTest;

public class ItemHost extends GameObjectHost implements Pickable, Item {
    private ItemProperty _property = new ItemProperty(){
        @Override
        public void setPickedUp(boolean pickedUp) {
            super.setPickedUp(pickedUp);
            getHeader().dirtyFlag |= isPickedUpDirtyFlag;
        }

        @Override
        public void setOwner(GameObject owner) {
            super.setOwner(owner);
            getHeader().dirtyFlag |= ownerDirtyFlag;
        }
    };
    private PickUpCondition _pickUpCondition = new PickUpTest();

    public ItemHost() {
        _property.setSkill(new HealthUpHost(getMatch()));
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
    public boolean getPickedUpBy(GameObject owner) {
        if (_property.isPickedUp()) return false;

        if (_pickUpCondition.evalulate(owner, this)){
            _property.setOwner(owner);
            _property.setPickedUp(true);
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
