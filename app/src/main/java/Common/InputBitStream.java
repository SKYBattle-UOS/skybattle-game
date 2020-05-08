package Common;

public interface InputBitStream {
    int read(int numBits);
    int read(byte[] data, int numBits);
    boolean isBufferOwner();
    byte[] getBuffer();
    void resetPos();
    void setBufferLength(int numBytes);
}