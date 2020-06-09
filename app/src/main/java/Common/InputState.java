package Common;

public class InputState {
    public int command;
    public int playerId = -1;
    public int lat;
    public int lon;

    public void writeToStream(OutputBitStream stream){
        // TODO
        stream.write(command, 4);
        stream.write(lat * lon != 0 ? 1 : 0, 1);
        if (lat * lon != 0){
            stream.write(lat, 32);
            stream.write(lon, 32);
        }
        stream.write(playerId >= 0 ? 1 : 0, 1);
        if (playerId >= 0)
            stream.write(playerId, 8);
    }

    public void readFromStream(InputBitStream stream){
        // TODO
        command = stream.read(4);
        if (stream.read(1) == 1){
            lat = stream.read(32);
            lon = stream.read(32);
        }

        if (stream.read(1) == 1){
            playerId = stream.read(8);
        }
    }
}
