package Common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WorldSetterHeader {
    static final int actionLen = WorldSetterAction.values().length - 1;
    static final WorldSetterAction[] actionValues = WorldSetterAction.values();

    public WorldSetterAction action;
    public int networkId;
    public int classId;

    public void setMembers(InputBitStream stream){
        byte[] bytes = new byte[4];
        stream.readBytes(bytes, 8);
        if (bytes[0] >= actionLen)
            action = WorldSetterAction.UNKNOWN;
        else
            action = actionValues[bytes[0]];

        stream.readBytes(bytes, 32);
        ByteBuffer bb = ByteBuffer.wrap(bytes, 0, 4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        networkId = bb.getInt();

        if (action == WorldSetterAction.CREATE){
            stream.readBytes(bytes, 32);
            bb = ByteBuffer.wrap(bytes, 0, 4);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            classId = bb.getInt();
        }
    }
}
