package com.example.Client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import Common.InputBitStream;
import Common.OutputBitStream;

public class NetworkPacketManager implements PacketManager {
    private Socket _socket;

    public void init(){
        // TODO
        try {
            _socket = new Socket("localhost", 9998);
            OutputStream stream = _socket.getOutputStream();
            stream.write(new byte[]{'a', 'b', 'c', 'd', 'x'});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputBitStream getPacketStream() {
        // TODO
        return null;
    }

    @Override
    public OutputBitStream getPacketToSend() {
        // TODO
        return null;
    }

    @Override
    public void update(long ms) {
        // TODO
    }
}
