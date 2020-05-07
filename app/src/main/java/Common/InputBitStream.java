package Common;

import java.io.IOException;

public interface InputBitStream {
    // TODO: delete readbytes later
    void readBytes(byte[] buffer, int numBits);

    int read(int numBits) throws IOException;
    int read(byte[] data, int numBits) throws IOException;
    byte[] getBuffer();
    void resetPos();
    void setBufferLength(int numBytes);
    boolean isBufferOwner();
}