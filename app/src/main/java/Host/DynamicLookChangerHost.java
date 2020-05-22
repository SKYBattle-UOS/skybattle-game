package Host;

import com.example.Client.ImageType;

import Common.DynamicLookChangerCommon;
import Common.GameObject;

public class DynamicLookChangerHost extends DynamicLookChangerCommon {
    protected DynamicLookChangerHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public static GameObject createInstance(){
        return new DynamicLookChangerHost(0, 0, "DynamicLookChanger");
    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
        if (!_nid2newImageType.isEmpty())
            _match.getWorldSetterHost().generateUpdateInstruction(getNetworkId(), 1 << 3);
    }

    @Override
    public void after(long ms) {

    }

    public void setLook(int networkId, ImageType type){
        _nid2newImageType.put(networkId, type);
    }
}
