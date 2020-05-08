package com.example.Client;

import android.content.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import Common.InputBitStream;

public class NetworkInstructionManager extends InstructionManager {
    private Socket _socket;

    NetworkInstructionManager(Context context, InputManager inputManager){
        super(context);
        // TODO
    }

    // TODO: DELETE
    public NetworkInstructionManager() {
        super(null);
    }

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
    public void update(long ms) {
        // TODO
    }
}
