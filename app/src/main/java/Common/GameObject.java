package Common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    public static int EndOfFlag;
    public static int EndOfFlagPos;

    static {
        int i = 0;
        posDirtyFlag = 1 << i++;
        nameDirtyFlag = 1 << i++;
        radiusDirtyFlag = 1 << i++;
        imageTypeDirtyFlag = 1 << i++;
        itemsDirtyFlag = 1 << i++;
        EndOfFlagPos = i;
        EndOfFlag = 1 << i++;
    }

    private float _radius = 2.5f;
    private int _networkId;
    private int _indexInWorld;
    private String _name = "GameObject";
    private double[] _position = new double[2];
    private boolean _wantsToDie;
    private boolean _collision;
    private ImageType _imageType;
    private ArrayList<Runnable> _onDeathListeners = new ArrayList<>();
    private ArrayList<Item> _items = new ArrayList<>();

    protected MatchCommon _match;
    protected double[] _restoreTemp = new double[2];

    private int[] _convertTemp = new int[2];

    public void writeToStream(OutputBitStream stream, int dirtyFlag){
        if ((dirtyFlag & posDirtyFlag) != 0) {
            double[] pos = getPosition();
            _match.getConverter().convertLatLon(pos[0], pos[1], _convertTemp);
            int lat = _convertTemp[0];
            int lon = _convertTemp[1];
            stream.write(lat, 32);
            stream.write(lon, 32);
        }

        if ((dirtyFlag & nameDirtyFlag) != 0){
            byte[] b = _name.getBytes(StandardCharsets.UTF_8);
            stream.write(b.length, 8);
            stream.write(b, b.length * 8);
        }

        if ((dirtyFlag & radiusDirtyFlag) != 0){
            float r = getRadius();
            int rInt = (int) (r * 10);
            stream.write(rInt, 16);
        }

        if ((dirtyFlag & imageTypeDirtyFlag) != 0){
            stream.write(_imageType.ordinal(), 4);
        }

        if ((dirtyFlag & itemsDirtyFlag) != 0){
            stream.write(_items.size(), 4);
            for (Item item : _items){
                stream.write(item.getGameObject().getNetworkId(), 32);
            }
        }
    }

    public void readFromStream(InputBitStream stream, int dirtyFlag){
    }

    public abstract void before(long ms);
    public abstract void update(long ms);
    public abstract void after(long ms);

    public void faceDeath(){
        if (_match.getCollider() != null && _collision)
            _match.getCollider().remove(this);
    }

    public void setOnDeathListener(@NonNull Runnable onDeathListener){
        _onDeathListeners.add(onDeathListener);
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

        for (Runnable r : _onDeathListeners)
            r.run();
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

    public void setPosition(double[] position){
        setPosition(position[0], position[1]);
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

    public List<Item> getItems(){
        return _items;
    }
    // endregion
}