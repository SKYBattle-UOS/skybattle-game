package Common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Client.Core;
import com.example.Client.ImageType;
import com.example.Client.RenderComponent;
import com.example.Client.Renderer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 매 프레임 Update 될 필요가 있는 객체들의 base abstract class 입니다.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public abstract class GameObject {
    public static int posDirtyFlag;
    public static int nameDirtyFlag;
    public static int radiusDirtyFlag;
    public static int imageTypeDirtyFlag;
    public static int itemsDirtyFlag;
    public static int startFromHereFlag;

    static {
        int i = 1;
        posDirtyFlag = i;
        i *= 2;
        nameDirtyFlag = i;
        i *= 2;
        radiusDirtyFlag = i;
        i *= 2;
        imageTypeDirtyFlag = i;
        i *= 2;
        itemsDirtyFlag = i;
        i *= 2;
        startFromHereFlag = i;
    }

    private float _radius = 2.5f;
    private String _name;
    private int _networkId;
    private int _indexInWorld;
    private double[] _position;
    private boolean _wantsToDie;
    private boolean _collision;
    private ImageType _imageType;
    private RenderComponent _renderComponent;
    private ArrayList<Runnable> _onDeathListeners = new ArrayList<>();
    private ArrayList<ItemCommon> _items = new ArrayList<>();

    protected MatchCommon _match;

    private double[] _restoreTemp = new double[2];
    private int[] _convertTemp = new int[2];

    protected GameObject(float latitude, float longitude, String name){
        _position = new double[]{ latitude, longitude };
        _name = name;
    }

    public void writeToStream(OutputBitStream stream, int dirtyFlag){
        if ((dirtyFlag & posDirtyFlag) != 0) {
            double[] pos = getPosition();
            _match.getConverter().convertLatLon(pos[0], pos[1], _convertTemp);
            int lat = _convertTemp[0];
            int lon = _convertTemp[1];
            try {
                stream.write(lat, 32);
                stream.write(lon, 32);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ((dirtyFlag & nameDirtyFlag) != 0){
            byte[] b = _name.getBytes(StandardCharsets.UTF_8);
            try {
                stream.write(b.length, 8);
                stream.write(b, b.length * 8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ((dirtyFlag & radiusDirtyFlag) != 0){
            float r = getRadius();
            int rInt = (int) r * 10;
            try {
                stream.write(rInt, 16);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ((dirtyFlag & imageTypeDirtyFlag) != 0){
            try {
                stream.write(_imageType.ordinal(), 4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ((dirtyFlag & itemsDirtyFlag) != 0){
            try {
                stream.write(_items.size(), 4);
                for (ItemCommon item : _items){
                    stream.write(item.getNetworkId(), 32);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        if ((dirtyFlag & posDirtyFlag) != 0) {
            int lat = stream.read(32);
            int lon = stream.read(32);
            _match.getConverter().restoreLatLon(lat, lon, _restoreTemp);
            setPosition(_restoreTemp[0], _restoreTemp[1]);
        }

        if ((dirtyFlag & nameDirtyFlag) != 0) {
            int len = stream.read(8);
            byte[] b = new byte[len];
            stream.read(b, len * 8);
            _name = new String(b, StandardCharsets.UTF_8);
        }

        if ((dirtyFlag & radiusDirtyFlag) != 0) {
            int rInt = stream.read(16);
            setRadius(((float) rInt) / 10f);
        }

        if ((dirtyFlag & imageTypeDirtyFlag) != 0) {
            ImageType type = ImageType.values()[stream.read(4)];
            setRenderComponent(Core.get().getRenderer().createRenderComponent(this, type));
        }

        if ((dirtyFlag & itemsDirtyFlag) != 0){
            _items.clear();
            int itemsSize = stream.read(4);
            for (int i = 0; i < itemsSize; i++){
                GameObject item = _match.getRegistry().getGameObject(stream.read(32));
                _items.add((ItemCommon) item);
            }
            itemsWereAdded();
        }
    }

    public abstract void before(long ms);
    public abstract void update(long ms);
    public abstract void after(long ms);

    public void faceDeath(){
        if (_renderComponent != null)
            _renderComponent.destroy();

        for (Runnable r : _onDeathListeners)
            r.run();
    }

    public void setOnDeathListener(@NonNull Runnable onDeathListener){
        _onDeathListeners.add(onDeathListener);
    }

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
    public void setCollision(){
        _collision = true;
    }

    public MatchCommon getMatch(){
        return _match;
    }

    public void setMatch(MatchCommon match){
        _match = match;
    }

    public void scheduleDeath(){
        _wantsToDie = true;
    }

    public boolean wantsToDie(){
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

        if (_match.getCollider() != null && _collision)
            _match.getCollider().positionDirty(this);
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
        if (_renderComponent != null)
            _renderComponent.destroy();
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

        if (_match.getCollider() != null && _collision)
            _match.getCollider().positionDirty(this);
    }

    public void setLook(ImageType type){
        _imageType = type;
    }

    public void addItem(ItemCommon item){
        _items.add(item);
    }

    public List<ItemCommon> getItems(){
        return _items;
    }

    protected void itemsWereAdded(){}
    // endregion
}
