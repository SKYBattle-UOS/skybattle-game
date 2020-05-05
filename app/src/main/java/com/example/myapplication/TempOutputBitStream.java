package com.example.myapplication;

import java.io.IOException;

public class TempOutputBitStream implements OutputBitStream {
    private byte[] _buffer = new byte[1300];

    @Override
    public void write(byte[] data, int numBits) throws IOException {

    }

    @Override
    public void write(int data, int numBits) throws IOException {

    }

    @Override
    public boolean isBufferOwner() {
        // TODO
        return true;
    }
}
