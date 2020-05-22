package Host;

import com.example.Client.ImageType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Common.GameObject;
import Common.OutputBitStream;

public class DynamicLookChangerHost extends GameObject {
    protected HashMap<Integer, ImageType> _nid2newImageType = new HashMap<>();

    protected DynamicLookChangerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public static GameObject createInstance(){
        return new DynamicLookChangerHost(0, 0, "DynamicLookChanger");
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);

        try {
            if ((dirtyFlag & (1 << _dirtyPos++)) != 0) {
                stream.write(_nid2newImageType.size(), 8);
                for (Map.Entry<Integer, ImageType> entry : _nid2newImageType.entrySet()){
                    stream.write(entry.getKey(), 32);
                    stream.write(entry.getValue().ordinal(), 4);
                }

                _nid2newImageType.clear();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {

    }

    @Override
    public void after(long ms) {

    }

    public void setLook(int networkId, ImageType type){
        _nid2newImageType.put(networkId, type);
        _match.getWorldSetterHost().generateUpdateInstruction(getNetworkId(), 1 << 3);
    }
}
