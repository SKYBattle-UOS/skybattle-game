package Common;

import java.nio.charset.StandardCharsets;

public class RoomUserInfo {
    public int playerId;
    public int team;
    public String name;

    public void writeToStream(OutputBitStream stream) {
        stream.write(playerId, 32);
        stream.write(team, 1);
        byte[] b = name.getBytes(StandardCharsets.UTF_8);
        stream.write(b.length, 8);
        stream.write(b, b.length * 8);
    }

    public void readFromStream(InputBitStream stream){
        playerId = stream.read(32);
        team = stream.read(1);
        int len = stream.read(8);
        byte[] b = new byte[len];
        stream.read(b, len * 8);
        name = new String(b, StandardCharsets.UTF_8);
    }
}
