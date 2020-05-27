package Host;

import Common.GameObject;

public class BuffableInteger {
    private GameObject _dataOwner;
    private int _data;

    public int getBuffedData(){
        return _data;
    }

    public int getRawData(){
        return _data;
    }

    public void setRawData(int data){
        _data = data;
    }
}
