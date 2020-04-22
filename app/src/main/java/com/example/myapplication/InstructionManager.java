package com.example.myapplication;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class InstructionManager {
    protected Context _context;

    InstructionManager(Context context) throws IOException {
        _context = context;
    }

    public abstract void sendInput();
    public abstract InputBitStream getPacketStream();
}