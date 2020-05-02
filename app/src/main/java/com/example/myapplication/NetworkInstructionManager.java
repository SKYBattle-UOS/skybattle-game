package com.example.myapplication;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetworkInstructionManager extends InstructionManager {
    NetworkInstructionManager(Context context, InputManager inputManager) throws IOException {
        super(context);
        // TODO
    }

    @Override
    public void sendInput(byte[] data) {
        // TODO
    }

    @Override
    public InputBitStream getPacketStream() {
        // TODO
        return null;
    }
}
