package com.example.myapplication;

public abstract class GameObject implements com.example.myapplication.Serializable {
    private float[] _position;
    private String _name;

    GameObject(float latitude, float longitude, String name){
        _position = new float[]{ latitude, longitude };
        _name = name;
    }

    public float[] getPosition(){
        return _position.clone();
    }

    public String getName(){
        return _name;
    }

    public void setPosition(float latitude, float longitude){
        _position[0] = latitude;
        _position[1] = longitude;
    }

    public void setName(String name){
        _name = name;
    }

    public void writeToStream(OutStream stream) {
        stream.write(_position[0]);
        stream.write(_position[1]);
        stream.write(_name);
    }

    public void readFromStream(InStream stream) {
        _position[0] = stream.readFloat();
        _position[1] = stream.readFloat();
        _name = stream.readString();
    }

    public abstract void update(int ms);
    public abstract void render(Renderer renderer);
}
