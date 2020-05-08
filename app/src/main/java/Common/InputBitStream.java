package Common;

public interface InputBitStream {
    int read(int numBits);
    int read(byte[] data, int numBits);
    boolean isBufferOwner();
    byte[] getBuffer();

    void resetPos(); //위치 초기화
    void setBufferLength(int numBytes); //최대 버퍼 저장 가능한 길이
    //버퍼는 1500 인경우 300 데이터가 있는 경우  버퍼를 새롭게 줬을 때 버퍼에 크기와 관련 없이 데이터의 양을 세팅 하겠금
    // 재활용용
}