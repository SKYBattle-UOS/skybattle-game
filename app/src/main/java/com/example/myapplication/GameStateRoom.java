package com.example.myapplication;

import android.util.Log;

import java.nio.ByteBuffer;

public class GameStateRoom implements GameState {
    GameStateContext _parent;

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
    }

    @Override
    public void update(int ms) {
        Log.i("Stub", "GameStateRoom: Showing Room Screen");

        InputBitStream packetStream = Core.getInstance().getInstructionManager().getPacketStream();
        if (packetStream == null) return;

        // TODO EDIT
        // region DEBUG
        byte[] buffer = new byte[4];
        packetStream.readBytes(buffer, 32);
        int message = ByteBuffer.wrap(buffer).getInt(); // big-endian

        // match start
        if (message == 42)
            _parent.switchState(GameStateType.MATCH);
        // endregion
    }
}
