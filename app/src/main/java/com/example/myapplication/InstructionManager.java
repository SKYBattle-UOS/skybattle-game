package com.example.myapplication;

import android.content.Context;


public abstract class InstructionManager {
    protected Context _context;

    InstructionManager(Context context)  {
        _context = context;
    }

    public abstract void sendInput(byte[] data);
    public abstract InputBitStream getPacketStream();
}