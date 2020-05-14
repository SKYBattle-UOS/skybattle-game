package com.example.Client;

import org.junit.Test;

import java.io.IOException;

import Common.BitInputStream;
import Common.BitOutputStream;

public class StreamTest {
    @Test
    public void stream(){
        BitOutputStream bitOut = new BitOutputStream();
        BitInputStream bitIn = new BitInputStream();

        try {
            bitOut.write(3, 3);
            bitOut.write(7, 3);
            bitOut.write(1, 3);
            bitOut.write(5, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buffer = bitOut.getBuffer();
        for (int i = 0; i < buffer.length; i++)
            System.out.println(buffer[i]);
    }
}
