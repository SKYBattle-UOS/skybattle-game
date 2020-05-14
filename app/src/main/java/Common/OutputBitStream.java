package Common;

import java.io.IOException;

public interface OutputBitStream {
    void write(byte[] data, int numBits) throws IOException;
    void write(int data, int numBits) throws IOException;
    int availableBits(); //남은 바이트-> 비트
    boolean isBufferOwner();
    byte[] getBuffer();
    int getBufferByteLength(); //데이터 길이
    void resetPos();
}