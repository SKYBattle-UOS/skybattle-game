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
    private final Object _updateInputMutex = new Object();
    private boolean _isUpdated = false;

    public void init(){
        try {
            _socket = new Socket("localhost", 9998);
            (new Thread(this::receive)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputBitStream getPacketStream() {
        synchronized (_updateInputMutex){
            if (!_isUpdated) return null;
            return _gotThisFrame;
        }
    }

    @Override
    public OutputBitStream getPacketToSend() {
        return _sendThisFrame;
    }

    @Override
    public void update() {
        try {
            OutputStream outStream = _socket.getOutputStream();
            outStream.write(_sendThisFrame.getBuffer(), 0, _sendThisFrame.getBufferByteLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        _sendThisFrame.resetPos();

        synchronized (_updateInputMutex){
            _isUpdated = false;
        }
    }

    private void receive() {
        while (true) {
            try {
                InputStream inStream = _socket.getInputStream();
                synchronized (_updateInputMutex) {
                    int readBytes = inStream.read(_gotThisFrame.getBuffer());
                    _gotThisFrame.setBufferLength(readBytes);
                    _isUpdated = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
