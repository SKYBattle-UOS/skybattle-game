package com.example.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.BitInputStream;
import Common.BitOutputStream;
import Common.InputBitStream;
import Common.OutputBitStream;
import Common.Util;

public class NetworkPacketManager implements PacketManager {
    private Socket _socket;
    private OutputBitStream _sendThisFrame = new BitOutputStream();
    private Queue<InputBitStream> _rawPackets = new ConcurrentLinkedQueue<>();
    private boolean _shouldSendThisFrame = false;

    public void init(String host){
        (new Thread(()->receive(host))).start();
    }

    @Override
    public InputBitStream getPacketStream() {
        return _rawPackets.peek();
    }

    @Override
    public OutputBitStream getPacketToSend() {
        return _sendThisFrame;
    }

    @Override
    public void shouldSendThisFrame() {
        _shouldSendThisFrame = true;
    }

    @Override
    public void update() {
        _rawPackets.poll();
        send();
        _sendThisFrame.resetPos();
    }

    private void send(){
        if (_socket == null) return;

        if (!_shouldSendThisFrame) return;

        try {
            OutputStream outStream = _socket.getOutputStream();
            outStream.write(_sendThisFrame.getBuffer(), 0, _sendThisFrame.getBufferByteLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        _shouldSendThisFrame = false;
    }

    private void receive(String host) {
        try {
            while (_socket == null)
                _socket = new Socket(host, Util.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                InputStream inStream = _socket.getInputStream();
                InputBitStream newPacket = new BitInputStream();
                int readBytes = inStream.read(newPacket.getBuffer());
                newPacket.setBufferLength(readBytes);
                _rawPackets.offer(newPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
