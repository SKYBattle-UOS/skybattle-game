package com.example.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Common.BitInputStream;
import Common.BitOutputStream;
import Common.InputBitStream;
import Common.OutputBitStream;

public class NetworkPacketManager implements PacketManager {
    private Socket _socket;
    private OutputBitStream _sendThisFrame = new BitOutputStream();
    private InputBitStream _gotThisFrame = new BitInputStream();
    private final Boolean _canUpdateInput = true;

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
        if (_canUpdateInput) return null;
        return _gotThisFrame;
    }

    @Override
    public OutputBitStream getPacketToSend() {
        return _sendThisFrame;
    }

    public void send() {
        // TODO
        try {
            OutputStream outStream = _socket.getOutputStream();
            outStream.write(_sendThisFrame.getBuffer(), 0, _sendThisFrame.getBufferByteLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        _sendThisFrame.resetPos();
    }

    // TODO: Synchronize
    private void receive() {
        while (true) {
            synchronized (_canUpdateInput){
                try {
                    InputStream inStream = _socket.getInputStream();
                    int readBytes = inStream.read(_gotThisFrame.getBuffer());
                    _gotThisFrame.setBufferLength(readBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
