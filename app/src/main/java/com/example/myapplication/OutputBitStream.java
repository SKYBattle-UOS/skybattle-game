package com.example.myapplication;

import java.io.IOException;

public interface OutputBitStream {
    void write(byte[] data, int numBits) throws IOException;
    void write(int data, int numBits) throws IOException;
    boolean isBufferOwner();
}