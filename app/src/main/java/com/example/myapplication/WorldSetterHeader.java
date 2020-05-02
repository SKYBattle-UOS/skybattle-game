package com.example.myapplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

enum WorldSetterAction {
    CREATE,
    UPDATE,
    DESTROY,
    UNKNOWN
}

public class WorldSetterHeader {
    static final int actionLen = WorldSetterAction.values().length - 1;
    static final WorldSetterAction[] actionValues = WorldSetterAction.values();

    WorldSetterAction action;
    int networkId;
    int classId;

    public void setMembers(InputBitStream stream){
        byte[] bytes = new byte[9];
        stream.readBytes(bytes, 72);
        if (bytes[0] >= actionLen)
            action = WorldSetterAction.UNKNOWN;
        else
            action = actionValues[bytes[0]];

        ByteBuffer bb = ByteBuffer.wrap(bytes, 1, 4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        networkId = bb.getInt();

        bb = ByteBuffer.wrap(bytes, 5, 4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        classId = bb.getInt();
    }
}
