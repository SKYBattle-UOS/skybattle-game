package com.example.myapplication;

public interface InputBitStream {
    void readBytes(byte[] buffer, int numBits);
    void reset();
}
