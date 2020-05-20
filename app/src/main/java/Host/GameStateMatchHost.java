package Host;

import com.example.Client.GameObjectFactory;
import com.example.Client.GameObjectRegistry;

import Common.Collider;
import Common.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import Common.GameState;
import Common.InputBitStream;
import Common.InputState;
import Common.MatchStateType;
import Common.PlayerHost;
import Common.Util;

public class GameStateMatchHost implements GameState {
    private GameStateContextHost _parent;
    private GameState _currentState;
    private WorldSetterHost _worldSetter;
    private ArrayList<GameObject> _gameObjects;
    private ArrayList<PlayerHost> _players;
    private GameObjectRegistry _registry;
    private GameObjectFactory _factory;
    private Collider _collider;
    private int _nextNetworkId = 1;
    private boolean _worldSetterActive = false;

    // TODO
    private int _numPlayers;
    private double[] _battleGroundLatLon;
    private final int GET_READY_COUNT;
    private final int NUM_PACKET_PER_FRAME;

    public GameStateMatchHost(GameStateContextHost parent){
        _parent = parent;
        _registry = new GameObjectRegistry();
        _worldSetter = new WorldSetterHost(_registry);
        _factory = new GameObjectFactory();
        _gameObjects = new ArrayList<>();
        _players = new ArrayList<>();
        _collider = new Collider();

        _numPlayers = CoreHost.getInstance().getNetworkManager().getNumConnections();
        _battleGroundLatLon = new double[2];
        GET_READY_COUNT = 10000;
        NUM_PACKET_PER_FRAME = 3;

        // TODO
        Util.registerGameObjectsHost(_factory);
        switchState(MatchStateType.ASSEMBLE);
    }

    public GameObject createGameObject(int classId){
        GameObject ret = _factory.createGameObject(classId);
        int networkId = _nextNetworkId++;

        ret.setCollider(_collider);
        ret.setNetworkId(networkId);
        ret.setWorldSetterHost(_worldSetter);
        ret.setIndexInWorld(_gameObjects.size());
        ret.setLatLonByteConverter(_parent.getConverter());

        _registry.add(networkId, ret);
        _gameObjects.add(ret);
        _worldSetter.generateCreateInstruction(classId, networkId, -1);

        return ret;
    }

    public void createPlayers() {
        Collection<ClientProxy> clients = CoreHost.getInstance().getNetworkManager().getClientProxies();
        for (ClientProxy client : clients){
            PlayerHost newPlayer = (PlayerHost) createGameObject(Util.PlayerClassId);
            newPlayer.setPlayerId(client.getPlayerId());
            newPlayer.setPosition(37.714580, 127.045195);
        }

        // create temp item
        GameObject tempItem = createGameObject(Util.ItemClassId);
        tempItem.setPosition(37.715584, 127.048616);
        tempItem.setName("여기여기 모여라");
    }

    public void setWorldSetterActive(){
        _worldSetterActive = true;
    }

    @Override
    public void update(long ms) {
        handleInputFromClients();

        if (_worldSetterActive)
            _worldSetter.writeInstructionToStream(CoreHost.getInstance().getNetworkManager().getPacketToSend());

        for (GameObject go : _gameObjects)
            go.before(ms);

        for (GameObject go : _gameObjects)
            go.update(ms);

        for (GameObject go : _gameObjects)
            go.after(ms);

        _collider.update(ms);
        _currentState.update(ms);
    }

    private void handleInputFromClients() {
        NetworkManager net = CoreHost.getInstance().getNetworkManager();
        Collection<ClientProxy> clients = net.getClientProxies();

        for (ClientProxy client : clients){
            Queue<InputBitStream> rawPacketQueue = client.getRawPacketQueue();
            Queue<InputBitStream> packetQueue = client.getPacketQueue();
            for (int i = 0; i < NUM_PACKET_PER_FRAME; i++){
                InputBitStream rawPacket = rawPacketQueue.poll();
                if (rawPacket == null)
                    break;
                handleInputPacket(client, rawPacket);
                packetQueue.offer(rawPacket);
            }
        }
    }

    private void handleInputPacket(ClientProxy client, InputBitStream packet) {
        Queue<InputState> inputList = client.getUnprocessedInputs();
        int numInputs = packet.read(2);
        for (int i = 0; i < numInputs; i++){
            InputState newInput = new InputState();
            newInput.readFromStream(packet);
            inputList.offer(newInput);
        }
    }

    public void switchState(MatchStateType matchState) {
        switch (matchState) {
            case ASSEMBLE:
                _currentState = new MatchStateAssembleHost(this, _numPlayers);
                break;
            case SELECT_CHARACTER:
                _currentState = new MatchStateSelectCharacterHost(this);
                break;
            case GET_READY:
                _currentState = new MatchStateGetReadyHost(this, GET_READY_COUNT);
                break;
            case INGAME:
                _currentState = new MatchStateInGameHost(this);
                break;
        }
        _currentState.start();
    }

    public Collection<GameObject> getGameObjects(){
        return _gameObjects;
    }
    public Collection<PlayerHost> getPlayers() { return _players; }

    public boolean isWorldSetterActive() {
        return _worldSetterActive;
    }

    public double[] getBattleGroundLatLon() {
        return _battleGroundLatLon;
    }

    public void setBattleGroundLatLon(double lat, double lon) {
        _battleGroundLatLon[0] = lat;
        _battleGroundLatLon[1] = lon;
        _parent.getConverter().setOffset(lat, lon);
    }

public Collider getCollider(){ return _collider; }
}
