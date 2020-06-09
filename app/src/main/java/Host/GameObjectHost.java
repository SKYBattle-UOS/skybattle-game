package Host;

import Common.GameObject;
import Common.ImageType;
import Common.WorldSetterHeader;

public abstract class GameObjectHost extends GameObject {
    private WorldSetterHeader _wsHeader;

    public WorldSetterHeader getHeader(){
        return _wsHeader;
    }

    public void setHeader(WorldSetterHeader header){
        _wsHeader = header;
    }

    @Override
    public void setLook(ImageType type) {
        super.setLook(type);
        _wsHeader.dirtyFlag |= imageTypeDirtyFlag;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        _wsHeader.dirtyFlag |= nameDirtyFlag;
    }

    @Override
    public void setPosition(double latitude, double longitude) {
        super.setPosition(latitude, longitude);
        _wsHeader.dirtyFlag |= posDirtyFlag;
    }

    @Override
    public void setRadius(float radius) {
        super.setRadius(radius);
        _wsHeader.dirtyFlag |= radiusDirtyFlag;
    }
}
