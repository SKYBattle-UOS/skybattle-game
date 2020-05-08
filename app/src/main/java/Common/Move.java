package Common;

public class Move {
    private float _timeStamp;
    private InputState _inputState;

    public Move(InputState inputState, float timeStamp){
        _inputState = inputState;
        _timeStamp = timeStamp;
    }

    public Move() {
        _timeStamp = 0;
        _inputState = null;
    }

    public float getTimeStamp(){
        return _timeStamp;
    }

    public void writeToStream(OutputBitStream stream){
        // TODO
        // stream.write(timeStamp);
        _inputState.writeToStream(stream);
    }

    public void readFromStream(InputBitStream stream){
        // TODO
        // _timeStamp = stream.read();
        _inputState.readFromStream(stream);
    }
}
