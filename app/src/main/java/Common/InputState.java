package Common;

import android.util.Log;

import java.io.IOException;

public class InputState {
    public int lat;
    public int lon;
    public boolean q, w, e, r;

    public void writeToStream(OutputBitStream stream){
        // TODO
        try {
            stream.write(lat, 32);
            stream.write(lon, 32);
            stream.write(q ? 1 : 0, 1);
            stream.write(w ? 1 : 0, 1);
            stream.write(e ? 1 : 0, 1);
            stream.write(r ? 1 : 0, 1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readFromStream(InputBitStream stream){
        // TODO
        lat = stream.read(32);
        lon = stream.read(32);
        q = stream.read(1) == 1;
        w = stream.read(1) == 1;
        e = stream.read(1) == 1;
        r = stream.read(1) == 1;
    }
}
