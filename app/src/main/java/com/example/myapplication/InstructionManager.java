package com.example.myapplication;

import android.content.Context;


public abstract class InstructionManager {
    protected Context _context;

    InstructionManager(Context context)  {
        _context = context;
    }

    public abstract InputBitStream getPacketStream();
    public abstract void update(long ms);
}