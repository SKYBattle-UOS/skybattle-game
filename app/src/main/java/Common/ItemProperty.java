package Common;

import com.example.Client.GameObjectRegistry;

public class ItemProperty {
    public static int isPickedUpDirtyFlag;
    public static int ownerDirtyFlag;
    public static int endOfFlag;
    public static int endOfFlagPos;

    static {
        int i = GameObject.EndOfFlagPos;
        isPickedUpDirtyFlag = 1 << i++;
        ownerDirtyFlag = 1 << i++;
        endOfFlagPos = i;
        endOfFlag = 1 << i++;
    }

    private boolean _isPickedUp;
    private GameObject _owner;
    private Skill _skill;

    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        if ((dirtyFlag & isPickedUpDirtyFlag) != 0){
            stream.write(isPickedUp() ? 1 : 0, 1);
        }

        if ((dirtyFlag & ownerDirtyFlag) != 0){
            if (_owner == null)
                stream.write(0, 32);
            else
                stream.write(_owner.getNetworkId(), 32);
        }
    }

    public void readFromStream(InputBitStream stream, int dirtyFlag, GameObjectRegistry registry) {
        if ((dirtyFlag & isPickedUpDirtyFlag) != 0){
            setPickedUp(stream.read(1) == 1);
        }

        if ((dirtyFlag & ownerDirtyFlag) != 0){
            _owner = registry.getGameObject(stream.read(32));
        }
    }

    public boolean isPickedUp() {
        return _isPickedUp;
    }

    public void setPickedUp(boolean pickedUp){
        _isPickedUp = pickedUp;
    }

    public Skill getSkill(){
        return _skill;
    }

    public void setSkill(Skill skill) {
        _skill = skill;
    }

    public void setOwner(GameObject owner){
        _owner = owner;
    }
}
