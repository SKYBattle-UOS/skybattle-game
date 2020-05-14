package Common;

import com.example.Client.RenderComponent;
import com.example.Client.Renderer;

/**
 * 매 프레임 Update 될 필요가 있는 객체들의 base abstract class 입니다.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public abstract class GameObject {
    public static int classId;

    private double[] _position;
    private String _name;
    private boolean _wantsToDie;
    private int _indexInWorld;
    private RenderComponent _renderComponent;
    private int _networkId;
    private int _dirtyFlag;

    GameObject(float latitude, float longitude, String name){
        _position = new double[]{ latitude, longitude };
        _name = name;
    }

    // region Getters and Setters
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

    public abstract void writeToStream(OutputBitStream stream, int dirtyFlag);
    public abstract void readFromStream(InputBitStream stream, int dirtyFlag);
    public abstract void update(long ms);

    public void scheduleDeath(){
        _wantsToDie = true;
    }

    public boolean doesWantToDie(){
        return _wantsToDie;
    }

    public void faceDeath(){}

    public void render(Renderer renderer){
        if (_renderComponent != null)
            renderer.batch(_renderComponent);
    }

    public static GameObject createInstance(){
        return null;
    };

}
