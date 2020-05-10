package Common;

import java.io.IOException;

public interface OutputBitStream {
    void write(byte[] data, int numBits) throws IOException;
    void write(int data, int numBits) throws IOException;
    int availableBits();
    boolean isBufferOwner();
}