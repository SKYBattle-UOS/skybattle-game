package Common;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Base class of bit output streams.
 * 플랫폼마다 바이트를 어떤 순서로 저장하는지 다름. -> 리틀 엔디언 ->  가장 작은 자리의 바이트부터 먼저 기재
 * @author HyungJoon
 */
public class BitOutputStream implements OutputBitStream {

    private static final int BITS_PER_BYTE = 8;
    private OutputStream out;
    private int buffer;
    private int bufferBitCount;
    private  boolean bufferOwner = false;

    private static final int[] MASKS = new int[] {
            0, 1, 3, 7, 0xf, 0x1f, 0x3f, 0x7f, 0xff
    };

    /**
     * OutputStream 을 받아서  OutputBitStream 초기화     *
     * @param out the OutputStream.
     */
    public BitOutputStream(OutputStream out) {
        this.out = out;
        this.bufferBitCount = 0;
    }

    public BitOutputStream() {
        this.out = new ByteArrayOutputStream(byteArrayToInt(new byte[1500]));
        this.bufferBitCount = 0;
        bufferOwner = true;
    }

    private int byteArrayToInt(byte[] bytes) {

        final int size = Integer.SIZE / 8;
        ByteBuffer buff = ByteBuffer.allocate(size);
        final byte[] newBytes = new byte[size];

        for (int i = 0; i < size; i++) {
            if (i + bytes.length < size) {
                newBytes[i] = (byte) 0x00;
            } else {
                newBytes[i] = bytes[i + bytes.length - size];
            }
        }
        buff = ByteBuffer.wrap(newBytes);
        buff.order(ByteOrder.LITTLE_ENDIAN ); //LITTLE_ENDIAN Endian에 맞게 세팅
        return buff.getInt();
    }

    /**
     * 비트들을 쓴다.
     * @param data          비트값인 data 를 저장한다..
     * @param numBits       쓰여질 비트 개수
     * @throws IOException  if an I/O error occurs.
     */
    public void write(int data, int numBits) throws IOException {
        while (numBits > 0) {
            int rest = BITS_PER_BYTE - bufferBitCount;  //여유공간 bit

            if (rest > numBits) {   //들어오는 비트보다 여유공간이 큰 경우
                buffer = ((data & MASKS[numBits]) << bufferBitCount) | buffer;
                bufferBitCount += numBits;
                numBits = 0;
            } else {    //여유공간이 없는 경우
                buffer = ((data & MASKS[rest]) << bufferBitCount) | buffer;
                out.write(buffer);  // 쓸 수 있는 공간이 없기에 꽉찬 버퍼를 씀.
                numBits -= rest;    //쓴 만큼 개수를 감소.
                data >>>= rest;
                bufferBitCount = 0; //초기화
                buffer = 0;
            }
        }
    }

    /**
     * 비트들을 쓴다.
     * @param data   쓰여질 바이트들
     * @param numBits       쓰여질 비트 개수
     * @throws IOException  if an I/O error occurs.
     */
    public void write(byte[] data, int numBits) throws IOException {
        int byteCount = 0;

        //바이트를 하나씩 모두 기록
        while (numBits > 8) {
            write(data[byteCount],8);
            ++byteCount;
            numBits -=8;
        }

        if(numBits > 0) //남은 비트들 기록
            write(data[byteCount],numBits);
    }

    public int availableBits(){
        int availableBit = 0;
        int maxSize = data.length;
        availableBit = (maxSize - byteOffset) * 8;
        availableBit += (8 - bitOffset);
        return availableBit;
    }

    public int getBufferByteLength(){
        out.
        ByteArrayOutputStream getBufferSize = out;
        return availableBit;
    }

    public boolean isBufferOwner(){
        return bufferOwner;
    }

    /**
     * 버퍼에 남은 비트들을 전부 OutputStream 에 쓰고 닫는다.
     */
    public void close() throws IOException {
        if (bufferBitCount > 0) {
            out.write(buffer);
        }

        if(bufferOwner)
            out.close();
    }
}