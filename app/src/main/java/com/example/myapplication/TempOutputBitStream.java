package com.example.myapplication;

public class TempOutputBitStream implements OutputBitStream {
    private byte[] _buffer;

    TempOutputBitStream(){
        _buffer = new byte[1300];
    }

    TempOutputBitStream(byte[] buffer){
        _buffer = buffer;
    }

    @Override
    public void writeBits(byte[] data, int numBits) {
        // TODO
    }

    @Override
    public void reset() {
        // TODO
    }
}
