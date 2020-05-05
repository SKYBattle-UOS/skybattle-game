package com.example.Client;

import Common.InputBitStream;
import Common.OutputBitStream;

public interface Serializable {
    void writeToStream(OutputBitStream stream);
    void readFromStream(InputBitStream stream);
}
