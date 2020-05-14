package Common;

import android.util.Log;

import java.io.IOException;

public class InputState {
    public double lat;
    public double lon;
    public boolean q, w, e, r;

    public void writeToStream(OutputBitStream stream){
        // TODO
        try {
            stream.write((int) lat, 8);
            stream.write((int) lon, 8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readFromStream(InputBitStream stream){
        // TODO
        lat = stream.read(8);
        lon = stream.read(8);
        Log.i("Stub", String.format("received %d", (int) lat));
    }
}
