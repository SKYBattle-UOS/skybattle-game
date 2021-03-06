package com.example.Client;

import Common.InputBitStream;
import Common.OutputBitStream;

public interface PacketManager {
    InputBitStream getPacketStream();
    OutputBitStream getPacketToSend();
    void shouldSendThisFrame();
    void update();
    int getPlayerId();
}