package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;

public class ReplayInstructionManager extends InstructionManager {
    private InputStream _inputStream;
    // TODO
    private TempInputBitStream[] _packets;
    private int _elapsed;
    private int _packetNum;
    private TempInputBitStream _packet;
    private TempOutputBitStream _outputStream;

    ReplayInstructionManager(Context context)  {
        super(context);

        _outputStream = new TempOutputBitStream();
        _packetNum = 0;
        _packets = new TempInputBitStream[30];
        for (int i = 0; i < _packets.length; i++)
            _packets[i] = new TempInputBitStream();

        _elapsed = 0;

        int i = 0;
        int p = 0;

        _packets[p].getBuffer()[i++] = 'a'; // start button pressed by host
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'r'; // replication
        _packets[p].getBuffer()[i++] = 0; // CREATE
        _packets[p].getBuffer()[i++] = 1; // network Id 1
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; // class Id 0
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 1; // position is dirty
        _packets[p].getBuffer()[i++] = 3; // lat
        _packets[p].getBuffer()[i++] = 3; // lon
        _packets[p].getBuffer()[i++] = 'r'; // replication
        _packets[p].getBuffer()[i++] = 0; // CREATE
        _packets[p].getBuffer()[i++] = 2; // network Id 2
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; // class Id 0
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 1; // position is dirty
        _packets[p].getBuffer()[i++] = 9; // lat
        _packets[p].getBuffer()[i++] = 9; // lon
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'z'; // not replication
        _packets[p].getBuffer()[i++] = 'i'; // every client has completed initialization of assemble
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'z'; // not replication
        _packets[p].getBuffer()[i++] = 's'; // assemble complete
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'r'; // replication
        _packets[p].getBuffer()[i++] = 2; // DESTROY
        _packets[p].getBuffer()[i++] = 1; // network Id 1
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packets[p].getBuffer()[i++] = 'r'; // replication
        _packets[p].getBuffer()[i++] = 1; // UPDATE
        _packets[p].getBuffer()[i++] = 2; // network Id 2
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 1; // position is dirty
        _packets[p].getBuffer()[i++] = 42; // lat
        _packets[p].getBuffer()[i++] = 42; // lon

        p++;
        i = 0;

//        // TODO
//        try {
//            _inputStream = _context.getAssets().open("saveFile.sav");
//            _inputStream.read(_tempBit2.getBuffer());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public InputBitStream getPacketStream() {
        // TODO
        return _packet;
    }

    public void update(int ms){
        // TODO
        _elapsed += ms;
        Log.i("Stub", String.format("ReplayInstructionManager: elapsed %d ms", _elapsed));

        if (_elapsed < 3000){
            _packet = null;
        }
        else if (_elapsed == 5000 || _elapsed == 9000 || _elapsed == 15000
            || _elapsed == 20000 || _elapsed == 23000 || _elapsed == 25000
            || _elapsed == 40000 || _elapsed == 41000 || _elapsed == 42000
            || _elapsed == 43000 || _elapsed == 44000 || _elapsed == 45000
            || _elapsed == 46000){
            _packet = _packets[_packetNum];
            _packetNum++;
        }
        else
            _packet = null;
    }
}