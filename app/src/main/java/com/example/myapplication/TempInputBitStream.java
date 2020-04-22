package com.example.myapplication;

public class TempInputBitStream implements InputBitStream {
    private byte[] _buffer = new byte[1300];

    @Override
    public void readBytes(byte[] buffer, int numBits) {
        // TODO
    }

    @Override
    public boolean isBufferOwner() {
        // TODO
        return true;
    }
}
