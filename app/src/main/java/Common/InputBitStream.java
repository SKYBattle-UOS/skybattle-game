package Common;

import java.io.IOException;

public interface InputBitStream {
    int read(int numBits);
    void read(byte[] data, int numBits);
    boolean isBufferOwner();
    byte[] getBuffer();
}