package Host;

import Common.Collider;
import Common.GameObject;

public abstract class GameObjectHost extends GameObject {
    protected WorldSetterHost _worldSetterHost;
    protected Collider _collider;

    protected GameObjectHost(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public void setWorldSetterHost(WorldSetterHost wsh){
        _worldSetterHost = wsh;
    }

    public void setCollider(Collider col){
        _collider = col;
        _collider.registerNew(this);
    }

    @Override
    public void setPosition(double latitude, double longitude) {
        super.setPosition(latitude, longitude);
        _collider.positionDirty(this);
    }
}
