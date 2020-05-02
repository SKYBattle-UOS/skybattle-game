package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ReplayInstructionManager extends InstructionManager {
    private InputStream _inputStream;
    // TODO
    private TempInputBitStream _tempBit;

    ReplayInstructionManager(Context context)  {
        super(context);

        _tempBit = new TempInputBitStream();

        // TODO
        try {
            _inputStream = _context.getAssets().open("saveFile.sav");
            _inputStream.read(_tempBit.getBuffer());
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
        return _tempBit;
    }
}