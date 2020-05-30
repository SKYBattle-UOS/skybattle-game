package com.example.Client;

import android.util.Log;

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
    private OutputBitStream _sendInThisFrame = new BitOutputStream();
    private Queue<InputBitStream> _rawPackets = new ConcurrentLinkedQueue<>();
    private InputBitStream _handleInThisFrame;
    private boolean _shouldSendThisFrame = false;

    public void init(String host){
        (new Thread(()->receive(host))).start();
    }

    @Override
    public InputBitStream getPacketStream() {
        return _handleInThisFrame;
    }

    @Override
    public OutputBitStream getPacketToSend() {
        return _sendInThisFrame;
    }

    @Override
    public void shouldSendThisFrame() {
        _shouldSendThisFrame = true;
    }

    @Override
    public void update() {
        _handleInThisFrame = _rawPackets.poll();
        send();
        _sendInThisFrame.resetPos();
    }

    private void send(){
        if (_socket == null) return;

        if (!_shouldSendThisFrame) return;

        try {
            OutputStream stream = _socket.getOutputStream();
            int packetByteLen = _sendInThisFrame.getBufferByteLength();
            byte[] sendThis = new byte[packetByteLen + 2];
            System.arraycopy(_sendInThisFrame.getBuffer(), 0, sendThis, 2, packetByteLen);

            sendThis[0] = (byte) (packetByteLen & 0xFF);
            sendThis[1] = (byte) ((packetByteLen >>> 8) & 0xFF);

            stream.write(sendThis, 0, sendThis.length);
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
                InputStream stream = _socket.getInputStream();
                while (true) {
                    InputBitStream newPacket = new BitInputStream();

                    int lbyte = stream.read();
                    int hbyte = stream.read();
                    int packetByteLen = (hbyte << 8) | lbyte;

                    int i = 0;
                    while (packetByteLen > 0) {
                        int b = stream.read();
                        newPacket.getBuffer()[i++] = (byte) b;
                        packetByteLen--;
                    }

                    Log.i("hehe", "received " + newPacket.getBuffer()[0]);
                    newPacket.setBufferLength(i);

                    _rawPackets.offer(newPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
