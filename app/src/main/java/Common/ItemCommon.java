package Common;

import java.io.IOException;

public abstract class ItemCommon extends GameObject implements Pickable {
    public static int isPickedUpDirtyFlag;
    public static int ownerDirtyFlag;
    public static int startFromHereFlag;

    static {
        int i = GameObject.startFromHereFlag;
        isPickedUpDirtyFlag = i;
        i *= 2;
        ownerDirtyFlag = i;
        i *= 2;
        startFromHereFlag = i;
    }

    protected boolean _isPickedUp;
    protected GameObject _owner;
    protected Skill _skill;

    protected ItemCommon(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);

        if ((dirtyFlag & isPickedUpDirtyFlag) != 0){
            try {
                stream.write(isPickedUp() ? 1 : 0, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ((dirtyFlag & ownerDirtyFlag) != 0){
            try {
                if (_owner == null)
                    stream.write(0, 32);
                else
                    stream.write(_owner.getNetworkId(), 32);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);

        if ((dirtyFlag & isPickedUpDirtyFlag) != 0){
            setPickedUp(stream.read(1) == 1);
        }

        if ((dirtyFlag & ownerDirtyFlag) != 0){
            _owner = _match.getRegistry().getGameObject(stream.read(32));
        }
    }

    @Override
    public boolean isPickedUp() {
        return _isPickedUp;
    }

    public void setPickedUp(boolean pickedUp){
        _isPickedUp = pickedUp;
    }

    @Override
    public void pickUp(GameObject owner) {
    }

    public Skill getSkill(){
        return _skill;
    }
}
