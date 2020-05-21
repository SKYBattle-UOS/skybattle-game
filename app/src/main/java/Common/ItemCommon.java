package Common;

import java.io.IOException;

public abstract class ItemCommon extends GameObject implements Pickable {
    protected boolean _isPickedUp;
    protected GameObject _owner;

    protected ItemCommon(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);

        if ((dirtyFlag & (1 << _dirtyPos++)) != 0){
            try {
                stream.write(_isPickedUp ? 1 : 0, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);

        if ((dirtyFlag & (1 << _dirtyPos++)) != 0){
            _isPickedUp = stream.read(1) == 1;
        }
    }

    @Override
    public boolean isPickedUp() {
        return _isPickedUp;
    }

    @Override
    public void pickUp(GameObject owner) {

    }
}
