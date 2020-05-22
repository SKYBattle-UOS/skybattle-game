package Common;

import com.example.Client.ImageType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class DynamicLookChangerCommon extends GameObject {
    protected HashMap<Integer, ImageType> _nid2newImageType = new HashMap<>();

    protected DynamicLookChangerCommon(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
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
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);

        if ((dirtyFlag & (1 << _dirtyPos++)) != 0) {
            int len = stream.read(8);
            for (int i = 0; i < len; i++){
                int networkId = stream.read(32);
                int imageType = stream.read(4);
                _nid2newImageType.put(networkId, ImageType.values()[imageType]);
            }
        }
    }
}
