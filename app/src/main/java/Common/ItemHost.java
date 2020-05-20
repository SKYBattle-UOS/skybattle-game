package Common;

import java.io.IOException;

public class ItemHost extends ItemCommon {
    protected ItemHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public static GameObject createInstance() {
        return new ItemHost(0, 0, "TempItem");
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        super.writeToStream(stream, dirtyFlag);

        if ((dirtyFlag & 8) != 0){
            try {
                if (_owner == null)
                    stream.write(0, 32);
                else
                    stream.write(_owner.getNetworkId(), 32);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
