package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ReplayInstructionManager extends InstructionManager {
    private InputStream _inputStream;

    ReplayInstructionManager(Context context) throws IOException {
        super(context);

        // TODO
        try {
            _inputStream = _context.getAssets().open("saveFile.sav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInput() {
        // nothing to send when playing a replay
    }

    @Override
    public InputBitStream getPacketStream() {
        // TODO
        return null;
    }
}
