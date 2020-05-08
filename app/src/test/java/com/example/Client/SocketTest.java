package com.example.Client;

import org.junit.Before;
import org.junit.Test;

import Host.NetworkManager;

public class SocketTest {
    NetworkManager server;
    NetworkPacketManager client;

    @Before
    public void setup(){
        server = new NetworkManager(9998);
        server.open();
        client = new NetworkPacketManager();
        client.init();
    }

    @Test
    public void test(){
//        while (!server._exit){
            // nothing
//        }
    }
}
