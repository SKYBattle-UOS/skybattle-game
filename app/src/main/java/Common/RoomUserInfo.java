package Common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RoomUserInfo {
    public int team;
    public String name;

    public void writeToStream(OutputBitStream stream) {
        try {
            stream.write(team, 1);
            byte[] b = name.getBytes(StandardCharsets.UTF_8);
            stream.write(b.length, 8);
            stream.write(b, b.length * 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromStream(InputBitStream stream){
        team = stream.read(1);
        int len = stream.read(8);
        byte[] b = new byte[len];
        stream.read(b, len * 8);
        name = new String(b, StandardCharsets.UTF_8);
    }
}
