package com.example.Client;

import android.util.Log;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;

public class GameStateRoom implements GameState {
    private GameStateContext _parent;

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
    }

    @Override
    public void update(long ms) {
        if (didHostPressStart()) {
            // assemble
            Log.i("Stub", "GameStateRoom: Start Button Pressed by Host");
            _parent.switchState(GameStateType.MATCH);
            Core.getInstance().getUIManager().switchScreen(ScreenType.ASSEMBLE);
        }
    }

    private boolean didHostPressStart(){
        InputBitStream packetStream = Core.getInstance().getPakcetManager().getPacketStream();
        if (packetStream != null && packetStream.availableBits() > 0)
            return packetStream.read(1) == 1;
        return false;
    }
}
