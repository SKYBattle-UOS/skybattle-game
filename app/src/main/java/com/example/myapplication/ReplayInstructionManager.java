package com.example.myapplication;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class ReplayInstructionManager extends InstructionManager {
    private InputStream _inputStream;
    // TODO
    private TempInputBitStream _temp;

    ReplayInstructionManager(Context context)  {
        super(context);

        _temp = new TempInputBitStream();

        _temp.getBuffer()[0] = 'z';
        _temp.getBuffer()[1] = 'a';
        _temp.getBuffer()[2] = 's';

//        // TODO
//        try {
//            _inputStream = _context.getAssets().open("saveFile.sav");
//            _inputStream.read(_tempBit2.getBuffer());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void sendInput() {
        // nothing to send when playing a replay
    }

    @Override
    public InputBitStream getPacketStream() {
        // TODO
        return _temp;
    }
}