package com.example.Client;

import Common.InputBitStream;
import Common.OutputBitStream;

/**
 * 매 프레임 Update 될 필요가 있는 객체들의 base abstract class 입니다.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public abstract class GameObject implements com.example.Client.Serializable {
    static int classId;

    private double[] _position;
    private String _name;
    private boolean _wantsToDie;
    private int _indexInWorld;

    /**
     * 간단한 constructor.
     * @param latitude 초기위치 : 위도
     * @param longitude 초기위치 : 경도
     * @param name 오브젝트 이름
     */
    GameObject(float latitude, float longitude, String name){
        _position = new double[]{ latitude, longitude };
        _name = name;
    }

    public double[] getPosition(){
        return _position.clone();
    }

    public String getName(){
        return _name;
    }

    public void setPosition(double latitude, double longitude){
        _position[0] = latitude;
        _position[1] = longitude;
    }

    public void setName(String name){
        _name = name;
    }

    public void setIndexInWorld(int index){
        _indexInWorld = index;
    }

    public int getIndexInWorld(){
        return _indexInWorld;
    }

    public void scheduleDeath(){
        _wantsToDie = true;
    }

    public boolean doesWantToDie(){
        return _wantsToDie;
    }

    /**
     * 스트림에 해당 객체를 Serialize 합니다.
     * @param stream OutStream 인터페이스를 만족하는 모든 스트림
     */
    public abstract void writeToStream(OutputBitStream stream);
    /**
     * 스트림에서 읽어서 해당 객체를 업데이트 합니다.
     * @param stream InStream 인터페이스를 만족하는 모든 스트림
     */
    public abstract void readFromStream(InputBitStream stream);

    public void faceDeath(){
        // by default nothing
    }

    /**
     * 매 프레임 호출되는 함수.
     * @param ms 지난 프레임부터 경과한 밀리세컨드.
     */
    public abstract void update(long ms);

    /**
     * 그래픽 렌더 시에 호출되는 함수.
     * @param renderer Renderer 객체 인스턴스.
     * @param ms
     */
    public abstract void render(Renderer renderer, long ms);

    public static GameObject createInstance(){
        return null;
    };
}
