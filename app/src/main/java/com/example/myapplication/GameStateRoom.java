package com.example.myapplication;

import android.util.Log;

public class GameStateRoom implements GameState {
    GameStateContext _parent;

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
    }

    @Override
    public void update(int ms) {
        InputBitStream packetStream = Core.getInstance().getInstructionManager().getPacketStream();
        if (packetStream == null) return;

        // TODO EDIT
        // region DEBUG
        byte[] buffer = new byte[1];
        packetStream.readBytes(buffer, 8);
        int message = buffer[0];

        // match start
        if (message == 'a') { // assemble
            Log.i("Stub", "GameStateRoom: Start Button Pressed by Host");
            _parent.switchState(GameStateType.MATCH);
        }
        // endregion
    }

    @Override
    public void render(Renderer renderer, int ms) {
        Log.i("Stub", "GameStateRoom: Showing Room Screen");
    }
}
