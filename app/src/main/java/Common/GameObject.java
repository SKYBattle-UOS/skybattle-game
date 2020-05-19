package Common;

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
    public static int classId;

    private String _name;
    private int _networkId;
    private int _indexInWorld;
    private double[] _position;
    private boolean _wantsToDie;
    private RenderComponent _renderComponent;
    protected WorldSetterHost _worldSetterHost;

    GameObject(float latitude, float longitude, String name){
        _position = new double[]{ latitude, longitude };
        _name = name;
    }

    public static GameObject createInstance(){ return null; };

    public void collisionEnter(GameObject other){}
    public void collisionExit(GameObject other){}

    public void writeToStream(OutputBitStream stream, int dirtyFlag){
        if ((dirtyFlag & 1) != 0) {
            double[] pos = getPosition();
            int lat = (byte) pos[0];
            int lon = (byte) pos[1];
            try {
                stream.write(lat, 8);
                stream.write(lon, 8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readFromStream(InputBitStream stream, int dirtyFlag){
        if ((dirtyFlag & 1) != 0){
            int lat = stream.read(8);
            int lon = stream.read(8);
            setPosition(lat, lon);
        }
    }

    public abstract void update(long ms);

    public void faceDeath(){}

    public void render(Renderer renderer){
        if (_renderComponent != null)
            renderer.batch(_renderComponent);
    }


    // region Getters and Setters
    public void scheduleDeath(){
        _wantsToDie = true;
    }

    public boolean doesWantToDie(){
        return _wantsToDie;
    }

    public void setWorldSetterHost(WorldSetterHost wsh){
        _worldSetterHost = wsh;
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
    // endregion
}
