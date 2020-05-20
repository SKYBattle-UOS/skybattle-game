package Common;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.Client.Core;
import com.example.Client.RenderComponent;
import com.example.Client.Renderer;

import java.io.IOException;

import Host.WorldSetterHost;

/**
 * 매 프레임 Update 될 필요가 있는 객체들의 base abstract class 입니다.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public abstract class GameObject {
    private float _radius = 2.5f;
    private String _name;
    private int _networkId;
    private int _indexInWorld;
    private double[] _position;
    private boolean _wantsToDie;
    private RenderComponent _renderComponent;
    protected LatLonByteConverter _converter;
    protected WorldSetterHost _worldSetterHost;
    protected Collider _collider;

    private double[] _restoreTemp = new double[2];
    private int[] _convertTemp = new int[2];

    protected GameObject(float latitude, float longitude, String name){
        _position = new double[]{ latitude, longitude };
        _name = name;
    }

    public void writeToStream(OutputBitStream stream, int dirtyFlag){
        if ((dirtyFlag & 1) != 0) {
            double[] pos = getPosition();
            _converter.convertLatLon(pos[0], pos[1], _convertTemp);
            int lat = _convertTemp[0];
            int lon = _convertTemp[1];
            try {
                stream.write(lat, 32);
                stream.write(lon, 32);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readFromStream(InputBitStream stream, int dirtyFlag){
        if ((dirtyFlag & 1) != 0){
            int lat = stream.read(32);
            int lon = stream.read(32);
            _converter.restoreLatLon(lat, lon, _restoreTemp);
            setPosition(_restoreTemp[0], _restoreTemp[1]);
        }
    }

    public abstract void before(long ms);
    public abstract void update(long ms);
    public abstract void after(long ms);

    public void faceDeath(){}

    public void render(Renderer renderer){
        if (_renderComponent != null)
            renderer.batch(_renderComponent);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof CollisionState)
            return this == ((CollisionState) obj).other;
        return super.equals(obj);
    }

    // region Getters and Setters
    public void setWorldSetterHost(WorldSetterHost wsh){
        _worldSetterHost = wsh;
    }

    public void setCollider(Collider col){
        _collider = col;
        _collider.registerNew(this);
    }

    public void scheduleDeath(){
        _wantsToDie = true;
    }

    public boolean doesWantToDie(){
        return _wantsToDie;
    }

    public double[] getPosition(){
        return _position.clone();
    }

    public String getName(){
        return _name;
    }

    public void setPosition(double latitude, double longitude){
        _position[0] = latitude;
        _position[1] = longitude;
        if (_collider != null)
            _collider.positionDirty(this);
    }

    public void setLatLonByteConverter(LatLonByteConverter converter){
        _converter = converter;
    }

    public void setName(String name){
        _name = name;
    }

    public void setIndexInWorld(int index){
        _indexInWorld = index;
    }

    public int getIndexInWorld(){
        return _indexInWorld;
    }

    public RenderComponent getRenderComponent() {
        return _renderComponent;
    }

    public void setRenderComponent(RenderComponent renderComponent) {
        this._renderComponent = renderComponent;
    }

    public int getNetworkId(){
        return _networkId;
    }

    public void setNetworkId(int networkId){
        _networkId = networkId;
    }

    public float getRadius() {
        return _radius;
    }

    public void setRadius(float radius) {
        this._radius = radius;
    }
    // endregion
}
