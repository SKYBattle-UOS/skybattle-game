package Common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WorldSetterHeader {
    static final int actionLen = WorldSetterAction.values().length - 1;
    static final WorldSetterAction[] actionValues = WorldSetterAction.values();

    public WorldSetterAction action;
    public int classId;
    public int networkId;
    public int dirtyFlag;

    public void readFromStream(InputBitStream stream){
        action = intToAction(stream.read(2));
        if (action == WorldSetterAction.CREATE)
            classId = stream.read(8);
        networkId = stream.read(8);
        dirtyFlag = stream.read(32);
    }

    public void writeToStream(OutputBitStream stream){
        try {
            stream.write(actionToInt(action), 2);
            if (action == WorldSetterAction.CREATE)
                stream.write(classId, 8);
            stream.write(networkId, 8);
            stream.write(dirtyFlag, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int actionToInt(WorldSetterAction action){
        return action.ordinal();
//        int actionInt = 3;
//        switch (action){
//            case CREATE:
//                actionInt = 0;
//                break;
//
//            case UPDATE:
//                actionInt = 1;
//                break;
//
//            case DESTROY:
//                actionInt = 2;
//                break;
//        }
//        return actionInt;
    }

    private WorldSetterAction intToAction(int i){
        return actionValues[i];
    }
}
