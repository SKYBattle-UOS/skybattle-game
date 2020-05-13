package Common;

/**
 * Base class of bit input streams.
 * 플랫폼마다 바이트를 어떤 순서로 저장하는지 다름. -> 리틀 엔디언 ->  가장 작은 자리의 바이트부터 먼저 기재
 * @author HyungJoon
 */

public class BitInputStream implements InputBitStream {

    private boolean bufferOwner = false;
    private byte[] data;

    private int byteOffset;
    private int bitOffset;
    private int byteLimit;

    /**
     * 인자 없는 경우 1300 바이트 배열 자체 생성 + 버퍼오너
     */
    public BitInputStream() {
        this.data = new byte[1300];
        bufferOwner = true;
    }

    /**
     * 매개변수로 바이트 배열 받을 시 내부 바이트 배열에 할당
     * @param  buffer input buffer
     */
    public BitInputStream(byte[] buffer) {
        this.data = buffer;
        byteLimit = buffer.length;
        bufferOwner = false;
    }

    /**
     * 32비트까지 읽을 수 있다.
     *
     * @param numBits 비트를 읽을 개수
     * @return 정수 반환 whose bottom n bits hold the read data.
     */
    @Override
    public int read(int numBits) {
        if (numBits == 0) {
            return 0;
        }

        int returnValue = 0;
        bitOffset += numBits;
        while (bitOffset > 8) {
            bitOffset -= 8;
            returnValue |= (data[byteOffset++] & 0xFF) << bitOffset;
            if(byteLimit != 0 && byteOffset == byteLimit )
            {
                returnValue &= 0xFFFFFFFF >>> (32 - numBits);
                return returnValue; //limit 값 넘은경우
            }
        }
        returnValue |= (data[byteOffset] & 0xFF) >> (8 - bitOffset);
        returnValue &= 0xFFFFFFFF >>> (32 - numBits);
        if (bitOffset == 8) {
            bitOffset = 0;
            byteOffset++;
        }
        return returnValue;
    }

    /**
     * Reads {@code numBits} bits into {@code buffer}.
     *  @param buffer 읽은 데이터들이 저장됨.
     * @param numBits 읽을 비트 개수.
     * @return
     */
    @Override
    public int read(byte[] buffer, int numBits) {
        // Whole bytes.
        int to = (numBits >> 3) /* numBits / 8 바이트 인덱스 얻음*/ ;
        for (int i = 0; i < to; i++) {
            buffer[i] = (byte) (data[byteOffset++] << bitOffset);
            buffer[i] = (byte) (buffer[i] | ((data[byteOffset] & 0xFF) >> (8 - bitOffset)));

            if(byteLimit != 0 && byteOffset == byteLimit ) {
                break;
            }
        }
        // Trailing bits.
        int bitsLeft = numBits & 7 /* numBits % 8 */;
        if (bitsLeft == 0) {
            return to;
        }
        // Set bits that are going to be overwritten to 0.
        buffer[to] = (byte) (buffer[to] & (0xFF >> bitsLeft));
        if (bitOffset + bitsLeft > 8) {
            // We read the rest of data[byteOffset] and increase byteOffset.
            buffer[to] = (byte) (buffer[to] | ((data[byteOffset++] & 0xFF) << bitOffset));
            bitOffset -= 8;
        }
        bitOffset += bitsLeft;
        int lastDataByteTrailingBits = (data[byteOffset] & 0xFF) >> (8 - bitOffset);
        buffer[to] |= (byte) (lastDataByteTrailingBits << (8 - bitsLeft));
        if (bitOffset == 8) {
            bitOffset = 0;
            byteOffset++;
        }
        return to;
    }

    public byte[] getBuffer(){
        return data;
    }

    public int availableBits(){
        int availableBitNum = 0;
       int maxSize = data.length;
        availableBitNum = (maxSize - byteOffset) * 8;
        availableBitNum += (8 - bitOffset);
        return availableBitNum;
    }

    @Override
    public void resetPos() {
        byteOffset = 0;
        bitOffset = 0;
    }

    @Override
    public void setBufferLength(int numBytes) {
        byteLimit = numBytes;
    }

    public boolean isBufferOwner(){
        return bufferOwner;
    }

    public void close(){
        if(bufferOwner)
            data = null;
    }
}