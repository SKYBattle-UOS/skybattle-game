package com.example.Client;

import Common.InputBitStream;

public interface PacketManager {
    InputBitStream getPacketStream();
    void update(long ms);
}