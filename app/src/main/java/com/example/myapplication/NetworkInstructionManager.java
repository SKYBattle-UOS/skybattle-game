package com.example.myapplication;

import android.content.Context;

import java.io.IOException;

public class NetworkInstructionManager extends InstructionManager {
    NetworkInstructionManager(Context context, InputManager inputManager) throws IOException {
        super(context);
        // TODO
    }

    @Override
    public InputBitStream getPacketStream() {
        // TODO
        return null;
    }

    @Override
    public void update(int ms) {
        // TODO
    }
}
