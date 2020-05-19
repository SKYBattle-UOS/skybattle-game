package Common;

public class Player extends GameObject {
    private int _playerId;

    public Player(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);
    }

    @Override
    public void update(long ms) {
    }

    public void setPlayerId(int playerId){
        _playerId = playerId;
    }
    public int getPlayerId(){ return _playerId; }
}
