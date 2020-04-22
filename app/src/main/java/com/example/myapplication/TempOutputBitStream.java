package com.example.myapplication;

public class TempOutputBitStream implements OutputBitStream {
    private byte[] _buffer = new byte[1300];

    @Override
    public void writeBits(byte[] data, int numBits) {
        // TODO
    }

    @Override
    public boolean isBufferOwner() {
        // TODO
        return true;
    }
}
