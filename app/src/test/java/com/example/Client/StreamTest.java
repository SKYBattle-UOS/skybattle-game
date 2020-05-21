package com.example.Client;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import Common.BitInputStream;
import Common.BitOutputStream;

public class StreamTest {
    @Test
    public void stream(){
        BitOutputStream bitOut = new BitOutputStream();

        try {
            bitOut.write(-1, 32);

            bitOut.write(1, 3);
            bitOut.write(2, 3);
            bitOut.write(3, 3);
            bitOut.write(4, 3);
            bitOut.write(5, 3);

            bitOut.write(1, 12);
            bitOut.write(2, 12);
            bitOut.write(3, 12);
            bitOut.write(4, 12);
            bitOut.write(5, 12);

            bitOut.write(1, 1);
            bitOut.write(2, 2);
            bitOut.write(3, 2);
            bitOut.write(4, 3);
            bitOut.write(5, 3);

            bitOut.write(1, 8);
            bitOut.write(2, 8);
            bitOut.write(3, 8);
            bitOut.write(4, 8);
            bitOut.write(5, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buffer = bitOut.getBuffer();
        BitInputStream bitIn = new BitInputStream(buffer);

        System.out.println(bitIn.read(32));

        for (int i = 0; i < 5; i++)
            System.out.println(bitIn.read(3));

        System.out.println();

        for (int i = 0; i < 5; i++)
            System.out.println(bitIn.read(12));

        System.out.println();

        System.out.println(bitIn.read(1));
        System.out.println(bitIn.read(2));
        System.out.println(bitIn.read(2));
        System.out.println(bitIn.read(3));
        System.out.println(bitIn.read(3));

        System.out.println();

        for (int i = 0; i < 5; i++)
            System.out.println(bitIn.read(8));

        System.out.println();

        bitOut.resetPos();

        String testString = "꿈나무";
        byte[] b = testString.getBytes(StandardCharsets.UTF_8);

        try {
            bitOut.write(12, 8);
            bitOut.write(12, 8);
            bitOut.write(13, 8);
            bitOut.write(13, 8);
            bitOut.write(14, 8);
            bitOut.write(14, 8);

            bitOut.write(-1, 8);
            bitOut.write(-2, 8);
            bitOut.write(-3, 8);
            bitOut.write(-4, 8);
            bitOut.write(-5, 8);

            bitOut.write(1, 3);
            bitOut.write(b.length, 8);
            bitOut.write(b, b.length * 8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buffer2 = bitOut.getBuffer();
        BitInputStream bitIn2 = new BitInputStream(buffer2);
        bitIn2.setBufferLength(buffer2.length);

        System.out.println(bitIn2.read(8));
        System.out.println(bitIn2.read(8));
        System.out.println(bitIn2.read(8));
        System.out.println(bitIn2.read(8));
        System.out.println(bitIn2.read(8));
        System.out.println(bitIn2.read(8));

        System.out.println(bitIn2.read(8) - 256);
        System.out.println(bitIn2.read(8) - 256);
        System.out.println(bitIn2.read(8) - 256);
        System.out.println(bitIn2.read(8) - 256);
        System.out.println(bitIn2.read(8) - 256);
        System.out.println(bitIn2.read(3));

        int len = bitIn2.read(8);
        byte[] rb = new byte[len];
        bitIn2.read(rb, len * 8);

        String anotherString = new String(rb, StandardCharsets.UTF_8);
        System.out.println(anotherString);
    }
}
