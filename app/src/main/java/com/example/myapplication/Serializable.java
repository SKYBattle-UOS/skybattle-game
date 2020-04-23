package com.example.myapplication;

public interface Serializable {
    void writeToStream(OutputBitStream stream);
    void readFromStream(InputBitStream stream);
}
