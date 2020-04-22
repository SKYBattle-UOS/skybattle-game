package com.example.myapplication;

public class TempInputBitStream implements InputBitStream {
    private byte[] _buffer;

    TempInputBitStream(byte[] buffer){
        _buffer = buffer;
    }

    @Override
    public void readBytes(byte[] buffer, int numBits) {
        // TODO
    }

    @Override
    public void reset(){
        // TODO
    }
}
