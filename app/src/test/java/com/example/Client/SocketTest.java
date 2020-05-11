package com.example.Client;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import Common.InputBitStream;
import Common.OutputBitStream;
import Host.ClientProxy;
import Host.NetworkManager;

public class SocketTest {
    NetworkManager server;
    NetworkPacketManager client;

    @Before
    public void setup(){
        server = new NetworkManager();
        server.open();
        client = new NetworkPacketManager();
        client.init();
    }

    @Test
    public void test(){
        OutputBitStream packet = client.getPacketToSend();

        try {
            packet.write(3, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.send();

        while (true){
            for (ClientProxy client : server.getClientProxies()){
                InputBitStream packetIn = client.getRawPacketQueue().poll();
                if (packetIn != null)
                    System.out.println(packetIn.read(5));
            }
        }
    }
}
