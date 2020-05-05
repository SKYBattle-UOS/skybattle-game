package com.example.Client;

import android.util.Log;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;

public class GameStateRoom implements GameState {
    GameStateContext _parent;

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
    }

    @Override
    public void update(long ms) {
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
            Core.getInstance().getUIManager().switchScreen(ScreenType.ASSEMBLE);
        }
        // endregion
    }

    @Override
    public void render(Renderer renderer, long ms) {
//        Log.i("Stub", "GameStateRoom: Showing Room Screen");
    }
}
