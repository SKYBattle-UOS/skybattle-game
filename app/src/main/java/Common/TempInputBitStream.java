package Common;

public class TempInputBitStream implements InputBitStream {
    private byte[] _buffer = new byte[1300];
    private int _nextByte = 0;

    @Override
    public int read(int numBits){
        return 0;
    }

    @Override
    public int read(byte[] data, int numBits){
        return 0;
    }

    @Override
    public int availableBits() {
        return 0;
    }

    @Override
    public boolean isBufferOwner() {
        // TODO
        return true;
    }

    public byte[] getBuffer(){
        return _buffer;
    }

    @Override
    public void resetPos() {

    }

    @Override
    public void setBufferLength(int numBytes) {

    }
}
