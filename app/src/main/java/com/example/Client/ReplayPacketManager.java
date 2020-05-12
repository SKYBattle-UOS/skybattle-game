package com.example.Client;

import android.util.Log;
import java.io.InputStream;
import Common.BitInputStream;
import Common.BitOutputStream;
import Common.InputBitStream;
import Common.OutputBitStream;

public class ReplayPacketManager implements PacketManager {
    private InputStream _inputStream;
    // TODO
    private BitInputStream[] _packets;
    private int[] _packetArrivalTime;
    private int _elapsed;
    private int _packetNum;
    private BitInputStream _packet;

    ReplayPacketManager()  {
        _packetNum = 0;
        _packets = new BitInputStream[30];
        _packetArrivalTime = new int[30];
        for (int i = 0; i < _packets.length; i++)
            _packets[i] = new BitInputStream();

        _elapsed = 0;

        int i = 0;
        int p = 0;

//        _packetArrivalTime[p] = 5000;
//        _packets[p].getBuffer()[i++] = 'a'; // start button pressed by host
//        p++;
//        i = 0;

        _packetArrivalTime[p] = 9000;
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
//
//        _packetArrivalTime[p] = 10000;
//        _packets[p].getBuffer()[i++] = 'z'; // not replication
//        _packets[p].getBuffer()[i++] = 'i'; // every client has completed initialization of assemble
//        p++;
//        i = 0;
//
        _packetArrivalTime[p] = 13000;
        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;
//
//        _packetArrivalTime[p] = 15000;
//        _packets[p].getBuffer()[i++] = 'z'; // not replication
//        _packets[p].getBuffer()[i++] = 's'; // assemble complete
//        p++;
//        i = 0;
//
//        _packetArrivalTime[p] = 25000;
//        _packets[p].getBuffer()[i++] = 'z'; // not replication
//        _packets[p].getBuffer()[i++] = 'c'; // character select complete
//        p++;
//        i = 0;

        _packetArrivalTime[p] = 40000;
        _packets[p].getBuffer()[i++] = 'r'; // replication
        _packets[p].getBuffer()[i++] = 2; // DESTROY
        _packets[p].getBuffer()[i++] = 1; // network Id 1
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        _packets[p].getBuffer()[i++] = 0; //
        p++;
        i = 0;

        _packetArrivalTime[p] = _packetArrivalTime[p - 1] + 1000;
        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packetArrivalTime[p] = _packetArrivalTime[p - 1] + 1000;
        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packetArrivalTime[p] = _packetArrivalTime[p - 1] + 1000;
        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packetArrivalTime[p] = _packetArrivalTime[p - 1] + 1000;
        _packets[p].getBuffer()[i++] = 'z'; // not replication
        p++;
        i = 0;

        _packetArrivalTime[p] = _packetArrivalTime[p - 1] + 1000;
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
    }

    @Override
    public InputBitStream getPacketStream() {
        // TODO
        return _packet;
    }

    @Override
    public OutputBitStream getPacketToSend() {
        // TODO: fake sending
        return null;
    }

    @Override
    public void update() {
        // TODO
    }

    public void update(long ms){
        if (_packetNum >= _packets.length)
            return;

        // TODO
        _packet = null;
        _elapsed += ms;

        if (_packetArrivalTime[_packetNum] < _elapsed){
            _packet = _packets[_packetNum++];
            Log.i("Stub", String.format("ReplayInstructionManager: packet arrived %d ms", _elapsed));
        }
    }
}