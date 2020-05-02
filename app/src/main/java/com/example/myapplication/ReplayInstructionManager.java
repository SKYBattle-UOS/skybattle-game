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

        int i = 0;
        _temp.getBuffer()[i++] = 'z'; // nothing
        _temp.getBuffer()[i++] = 'a'; // start button pressed by host
        _temp.getBuffer()[i++] = 'z'; // nothing
        _temp.getBuffer()[i++] = 'z'; // nothing
        _temp.getBuffer()[i++] = 'i'; // initialize players for assemble
        _temp.getBuffer()[i++] = 0; // CREATE
        _temp.getBuffer()[i++] = 1; // network Id 1
        _temp.getBuffer()[i++] = 0; //
        _temp.getBuffer()[i++] = 0; //
        _temp.getBuffer()[i++] = 0; //
        _temp.getBuffer()[i++] = 0; // class Id 0
        _temp.getBuffer()[i++] = 0; //
        _temp.getBuffer()[i++] = 0; //
        _temp.getBuffer()[i++] = 0; //
        _temp.getBuffer()[i++] = 'z'; // nothing
        _temp.getBuffer()[i++] = 'z'; // nothing
        _temp.getBuffer()[i++] = 's'; // assemble complete

//        // TODO
//        try {
//            _inputStream = _context.getAssets().open("saveFile.sav");
//            _inputStream.read(_tempBit2.getBuffer());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void sendInput(byte[] data) {
        // nothing to send when playing a replay
    }

    @Override
    public InputBitStream getPacketStream() {
        // TODO
        return _temp;
    }
}