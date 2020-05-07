package Common;

public class Move {
    private float _timeStamp;
    private InputState _inputState;

    public Move(InputState inputState, float timeStamp){
        _inputState = inputState;
        _timeStamp = timeStamp;
    }

    public float getTimeStamp(){
        return _timeStamp;
    }

    public void writeToStream(OutputBitStream stream){
        // TODO
    }

    public void readFromStream(InputBitStream stream){
        // TODO
    }
}
