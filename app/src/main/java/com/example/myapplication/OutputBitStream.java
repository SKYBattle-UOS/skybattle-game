package com.example.myapplication;

public interface OutputBitStream {
    void writeBits(byte[] data, int numBits);
    boolean isBufferOwner();
}