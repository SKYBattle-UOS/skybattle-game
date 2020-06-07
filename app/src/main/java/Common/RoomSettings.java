package Common;

import java.nio.charset.StandardCharsets;

public class RoomSettings {
    public String roomTitle;
    public boolean roomTitleChanged;
    public boolean startButtonPressed;

    public boolean writeToStream(OutputBitStream stream) {
        int dirtyFlag = 0;
        if (roomTitleChanged)
            dirtyFlag |= 1;

        if (startButtonPressed)
            dirtyFlag |= 2;

        stream.write(dirtyFlag, 10);

        if (roomTitleChanged) {
            byte[] b = roomTitle.getBytes(StandardCharsets.UTF_8);
            stream.write(b.length, 8);
            stream.write(b, b.length * 8);
        }

        return dirtyFlag != 0;
    }

    public boolean readFromStream(InputBitStream stream) {
        int dirtyFlag = stream.read(10);

        if ((dirtyFlag & 1) != 0) {
            roomTitleChanged = true;
            int titleLen = stream.read(8);
            byte[] b = new byte[titleLen];
            stream.read(b, titleLen * 8);
            roomTitle = new String(b, StandardCharsets.UTF_8);
        }

        if ((dirtyFlag & 2) != 0) {
            startButtonPressed = true;
        }

        return dirtyFlag != 0;
    }
}
