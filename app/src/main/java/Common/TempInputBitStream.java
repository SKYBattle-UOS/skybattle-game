package Common;

import java.io.IOException;

public class TempInputBitStream implements InputBitStream {
    private byte[] _buffer = new byte[1300];
    private int _nextByte = 0;

    @Override
    public void readBytes(byte[] buffer, int numBits) {
        int bytes = numBits / 8;

        for (int i = 0; i < bytes; i++)
            buffer[i] = _buffer[_nextByte + i];

        _nextByte += bytes;
    }

    @Override
    public int read(int numBits) throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] data, int numBits) throws IOException {
        return 0;
    }

    @Override
    public void resetPos() {

    }

    @Override
    public void setBufferLength(int numBytes) {

    }

    @Override
    public boolean isBufferOwner() {
        // TODO
        return true;
    }

    public byte[] getBuffer(){
        return _buffer;
    }
}
