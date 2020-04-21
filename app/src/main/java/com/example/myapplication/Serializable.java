package com.example.myapplication;

public interface Serializable {
    void writeToStream(OutStream stream);
    void readFromStream(InStream stream);
}
