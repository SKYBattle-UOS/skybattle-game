package com.example.myapplication;

public interface Serializable extends java.io.Serializable {
    void writeToStream(OutStream stream);
    void readFromStream(InStream stream);
}
