package com.example.Client;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

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
    private int _playerId;

    public void init(String host, Consumer<Boolean> onConnected){
        (new Thread(()->receive(host, onConnected))).start();
    }

    public void close(){
        try {
            _socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public int getPlayerId() {
        return _playerId;
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

    private void receive(String host, Consumer<Boolean> onConnected) {
        try {
            _socket = new Socket();
            _socket.connect(
                    new InetSocketAddress(
                            host.equals("localhost") ? "localhost" : "10.0.2.2", Util.PORT), 2000);
        } catch (IOException e) {
            onConnected.accept(false);
            return;
        }

        try {
            InputStream stream = _socket.getInputStream();
            _playerId = stream.read();
            if (_playerId < 0)
                onConnected.accept(false);
            else
                onConnected.accept(true);

            while (true) {
                InputBitStream newPacket = new BitInputStream();

                int lbyte = stream.read();
                int hbyte = stream.read();
                int packetByteLen = (hbyte << 8) | lbyte;

                if (lbyte < 0 || hbyte < 0)
                    throw new IOException();

                int i = 0;
                while (packetByteLen > 0) {
                    int b = stream.read();
                    newPacket.getBuffer()[i++] = (byte) b;
                    packetByteLen--;
                }

                newPacket.setBufferLength(i);

                _rawPackets.offer(newPacket);
            }
        } catch (IOException e) {
            // socket closed
            Log.i("hehe", "client closing");
        }
    }
}
