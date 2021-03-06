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
    private int byteSize;

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

    public void write(int data, int numBits) {
        while (numBits > 0) {
            int rest = BITS_PER_BYTE - bufferBitCount;  //여유공간 bit

            if (rest > numBits) {   //들어오는 비트보다 여유공간이 큰 경우
                buffer = ((data & MASKS[numBits]) << bufferBitCount) | buffer;
                bufferBitCount += numBits;
                numBits = 0;
            } else {    //여유공간이 없는 경우
                buffer = ((data & MASKS[rest]) << bufferBitCount) | buffer;
                try {
                    out.write(buffer);  // 쓸 수 있는 공간이 없기에 꽉찬 버퍼를 씀.
                } catch (IOException e) {
                    e.printStackTrace();
                    Util.exit();
                }
                byteSize++;
                numBits -= rest;    //쓴 만큼 개수를 감소.
                data >>>= rest;
                bufferBitCount = 0; //초기화
                buffer = 0;
            }
        }
    }

    public void write(byte[] data, int numBits) {
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

    @Override
    public int availableBits(){
        ByteArrayOutputStream buffer = (ByteArrayOutputStream) out;
        byte[] data = buffer.toByteArray();

        int availableBit = 0;
        int maxSize = data.length;
        availableBit = maxSize * 8;
        return availableBit;
    }

    @Override
    public int getBufferByteLength(){
        return bufferBitCount > 0 ? byteSize + 1 : byteSize;
    }

    @Override
    public byte[] getBuffer(){
        if (byteSize <= 0 && bufferBitCount == 0)
            return null;

        ByteArrayOutputStream ret = new ByteArrayOutputStream();
        try {
            ((ByteArrayOutputStream) out).writeTo(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bufferBitCount > 0)
            ret.write(buffer);
        return ret.toByteArray();
    }

    @Override
    public void resetPos() {
        try{
            out = new ByteArrayOutputStream(byteArrayToInt(new byte[1500]));
//            out.flush(); //이 부분은 이렇게 해도 되는지 조금 생각 필요해보임
            bufferBitCount = 0;
            byteSize = 0;
            buffer = 0;
        } catch (Exception e){}

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

/*
package Common;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Base class of bit output streams.
 * 플랫폼마다 바이트를 어떤 순서로 저장하는지 다름. -> 리틀 엔디언 ->  가장 작은 자리의 바이트부터 먼저 기재
 * @author HyungJoon
 */

/*
public class BitOutputStream implements OutputBitStream {

    private static final int BITS_PER_BYTE = 8;
    private byte[] data;
    private int byteOffset;
    private int bitOffset;
    private  boolean bufferOwner = false;

    private static final int[] MASKS = new int[] {
            0, 1, 3, 7, 0xf, 0x1f, 0x3f, 0x7f, 0xff
    };

    /**
     * OutputStream 을 받아서  OutputBitStream 초기화
     */

/*
    public BitOutputStream(byte[] data) {
        this.data = data;
        for (byte item:data) {
            if(item!=0)
                byteOffset++;
            else
                break;
        }
    }

    public BitOutputStream() {
        this.data = new byte[1500];
        this.byteOffset = 0;
        this.bitOffset = 0;
        bufferOwner = true;
    }
/*
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
     * @param dataValue          비트값인 data 를 저장한다..
     * @param numBits       쓰여질 비트 개수
     * @throws IOException  if an I/O error occurs.
     */

/*
    public void write(int dataValue, int numBits) throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        int tempBuffer = 0;
        while (numBits > 0) {
            int rest = BITS_PER_BYTE - bitOffset;  //여유공간 bit

            if (rest > numBits) {   //들어오는 비트보다 여유공간이 큰 경우
                tempBuffer = ((dataValue & MASKS[numBits]) << bitOffset) | tempBuffer;
                bitOffset += numBits;
                numBits = 0;
            } else {    //여유공간이 없는 경우
                tempBuffer = ((dataValue & MASKS[rest]) << bitOffset) | tempBuffer;
                out.write(tempBuffer);  // 쓸 수 있는 공간이 없기에 꽉찬 버퍼를 씀.
                byteOffset++;
                numBits -= rest;    //쓴 만큼 개수를 감소.
                dataValue >>>= rest;
                bitOffset = 0; //초기화
                tempBuffer = 0;
            }
        }
    }

    /**
     * 비트들을 쓴다.
     * @param data   쓰여질 바이트들
     * @param numBits       쓰여질 비트 개수
     * @throws IOException  if an I/O error occurs.
     */
/*
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

    @Override
    public int availableBits(){
        int availableBitNum = 0;
        int maxSize = data.length;
        availableBitNum = (maxSize - byteOffset) * 8;
        availableBitNum += (8 - bitOffset);
        return availableBitNum;
    }

    @Override
    public int getBufferByteLength(){
        return bitOffset > 0 ? byteOffset + 1 : byteOffset;
    }

    @Override
    public byte[] getBuffer(){
        return data;
    }

    @Override
    public void resetPos() {
        byteOffset = 0;
        bitOffset = 0;
    }

    public boolean isBufferOwner(){
        return bufferOwner;
    }

    /**
     * 버퍼에 남은 비트들을 전부 OutputStream 에 쓰고 닫는다.
     */
/*
    public void close() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        if ( byteOffset > 0 || bitOffset > 0) {
            out.write(data);
        }

        if(bufferOwner)
            out.close();
    }
}*/