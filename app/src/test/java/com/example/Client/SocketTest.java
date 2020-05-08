package com.example.Client;

import android.net.Network;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Host.NetworkManager;

import static org.junit.Assert.*;

public class SocketTest {
    NetworkManager server;
    NetworkInstructionManager client;

    @Before
    public void setup(){
        server = new NetworkManager(9998);
        server.init();
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
