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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readFromStream(InputBitStream stream){
        // TODO
        lat = stream.read(32);
        lon = stream.read(32);
    }
}
