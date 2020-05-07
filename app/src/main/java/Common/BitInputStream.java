package Common;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class of bit input streams. *
 * 플랫폼마다 바이트를 어떤 순서로 저장하는지 다름. -> 리틀 엔디언 ->  가장 작은 자리의 바이트부터 먼저 기재
 * @author HyungJoon
 */

public class BitInputStream implements InputBitStream {

    private static final int BITS_PER_BYTE = 8;
    private InputStream in;
    private int buffer;
    private int bufferBitCount;

    private int markedBuffer = 0;
    private int markedBufferBitCount = 0;
    private boolean bufferOwner = false;

    // 버퍼 오너면 비트스트림

    public BitInputStream() {
        this.in = new ByteArrayInputStream(new byte[1300]);
        this.bufferBitCount = 0;
        bufferOwner = true;
    }

    /**
     * Initializes a bit input stream from an InputStream.
     * @param  buffer input
     */
    public BitInputStream(byte[] buffer) {
        this.in = new ByteArrayInputStream(buffer);
        this.bufferBitCount = 0;
        bufferOwner = false;
    }

    //리틀엔디안
    private static final int[] MASKS = new int[] {
            0, 1, 3, 7, 0xf, 0x1f, 0x3f, 0x7f
    };

    @Override
    public void readBytes(byte[] buffer, int numBits) {

    }

    /**
     * 비트들을 읽는다.
     *
     * If available bits are not zero but less than numBits, this method will
     * return only available bits. No EOFException are thrown.
     *
     * @param numBits       bits count to be read. Must not larger than 32.
     * @return              an integer whose lower bits are bits read from stream.
     * @throws EOFException if no data available from the InputStream.
     * @throws IOException  if I/O error occurs.
     */
    public int read(int numBits) throws IOException {
        int result = 0;
        int resultBits = 0;
        boolean resultContainData = false;

        while (numBits > 0) {
            if (bufferBitCount == 0) {
                buffer = in.read();
                if (buffer < 0) {
                    if (!resultContainData) {
                        throw new EOFException();
                    }
                    return result;
                }
                bufferBitCount = BITS_PER_BYTE;
            }

            if (bufferBitCount > numBits) { //읽을 비트가 bit카운트 보다 적은경우
                result = ((buffer & MASKS[numBits]) << resultBits) | result;
                resultBits += numBits;
                bufferBitCount -= numBits;
                buffer >>= numBits;
                numBits = 0;
            } else {    //읽을 비트가 bit 카운트 보다 많음.
                result = (buffer << resultBits) | result;
                resultBits += bufferBitCount;
                numBits -= bufferBitCount;
                bufferBitCount = 0;
            }
            resultContainData = true;
        }
        return result;
    }

    public int read(byte[] data, int numBits) throws IOException {
        this.in = new ByteArrayInputStream(data);
        int result = 0;

        while (numBits > 8) {
            result += read(8);
            numBits -=8;
        }

        if(numBits > 0) //남은 비트들 읽기
            result +=read(numBits);

        return result;
    }

    @Override
    public void resetPos() {

    }

    @Override
    public void setBufferLength(int numBytes) {

    }

    public byte[] getBuffer() {
        //todo
        //return 0;
        return null;
    }

    /**
     * @return if this bit input stream supports mark and reset.
     */
    public boolean markSupported() {
        return in.markSupported();
    }

    /**
     * bit input stream 현재 위치를 저장 한다.
     * @param readLimit     the maximum limit of bits that can read before the mark
     *                      position become invalid.
     * @see   java.io.InputStream#mark(int)
     */
    public void mark(int readLimit) {
        in.mark((readLimit + BITS_PER_BYTE - 1) / BITS_PER_BYTE);
        markedBuffer = buffer;
        markedBufferBitCount = bufferBitCount;
    }

    /**
     * 현재 위치를 지정된 위치로 변경
     * If this method is called without calling mark, an IOException is thrown or
     * current position is set to start of the stream, which depends on behaviors of
     * the InputStream.
     *
     * @throws IOException   if this stream has not been marked or if the
     *                       mark has been invalidated.
     * @see    java.io.InputStream#reset()
     */
    public void reset() throws IOException {
        in.reset();
        buffer = markedBuffer;
        bufferBitCount = markedBufferBitCount;
    }

    public boolean isBufferOwner(){
        return bufferOwner;
    }

    /**
     * @return available bits in the stream.
     * @throws IOException if I/O error occurs.
     */
    public int availableBits() throws IOException {
        return (in.available() * BITS_PER_BYTE) + bufferBitCount;
    }

    /**
     * InputStream 닫는다.
     */
    public void close() throws IOException {
        if(bufferOwner)
            in.close();
    }
}