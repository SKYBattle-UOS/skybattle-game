package com.example.Client;

import org.junit.Before;
import org.junit.Test;

import Host.NetworkManager;

public class SocketTest {
    NetworkManager server;
    NetworkInstructionManager client;

    @Before
    public void setup(){
        server = new NetworkManager(9998);
        server.open();
        client = new NetworkInstructionManager();
        client.init();
    }

    @Test
    public void test(){
//        while (!server._exit){
            // nothing
//        }
    }
}
