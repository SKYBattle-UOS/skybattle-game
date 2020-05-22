package Host;

import com.example.Client.GameObjectFactory;
import com.example.Client.GameObjectRegistry;
import com.example.Client.ImageType;

import Common.Collider;
import Common.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import Common.GameState;
import Common.InputBitStream;
import Common.InputState;
import Common.LatLonByteConverter;
import Common.Match;
import Common.MatchStateType;
import Common.PlayerCommon;
import Common.PlayerHost;
import Common.Util;

public class GameStateMatchHost implements GameState, Match {
    private GameStateContextHost _parent;
    private GameState _currentState;
    private WorldSetterHost _worldSetter;
    private ArrayList<GameObject> _gameObjects;
    private ArrayList<GameObject> _newGameObjects;
    private ArrayList<Integer> _newGOClassId;
    private ArrayList<PlayerCommon> _players;
    private GameObjectRegistry _registry;
    private GameObjectFactory _factory;
    private Collider _collider;
    private DynamicLookChangerHost _lookChanger;
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
        _newGameObjects = new ArrayList<>();
        _newGOClassId = new ArrayList<>();
        _players = new ArrayList<>();
        _collider = new Collider();

        _numPlayers = CoreHost.getInstance().getNetworkManager().getNumConnections();
        _battleGroundLatLon = new double[2];
        GET_READY_COUNT = 1000;
        NUM_PACKET_PER_FRAME = 3;

        // TODO
        Util.registerGameObjectsHost(_factory);
        switchState(MatchStateType.ASSEMBLE);
    }

    @Override
    public GameObject createGameObject(int classId, boolean addToCollider){
        GameObject ret = _factory.createGameObject(classId);
        int networkId = _nextNetworkId++;

        ret.setMatch(this);
        ret.setNetworkId(networkId);

        if (addToCollider) {
            _collider.registerNew(ret);
            ret.setCollision();
        }

        _newGameObjects.add(ret);
        _newGOClassId.add(classId);

        return ret;
    }

    private void addNewGameObjectsToWorld(){
        int i = 0;
        for (GameObject go : _newGameObjects){
            _registry.add(go.getNetworkId(), go);
            go.setIndexInWorld(_gameObjects.size());
            _gameObjects.add(go);
            _worldSetter.generateCreateInstruction(_newGOClassId.get(i++), go.getNetworkId(), -1);
        }

        _newGameObjects.clear();
        _newGOClassId.clear();
    }

    public void createPlayers() {
        _lookChanger = (DynamicLookChangerHost) createGameObject(Util.DynamicLookChangerClassId, false);
        Collection<ClientProxy> clients = CoreHost.getInstance().getNetworkManager().getClientProxies();

        int i = 1;
        for (ClientProxy client : clients){
            PlayerHost newPlayer = (PlayerHost) createGameObject(Util.PlayerClassId, true);
            newPlayer.setPlayerId(client.getPlayerId());
            newPlayer.setPosition(37.714580, 127.045195);
            newPlayer.setName("플레이어" + i);
            _lookChanger.setLook(newPlayer.getNetworkId(), ImageType.MARKER);
        }

        // create temp item
        GameObject tempItem = createGameObject(Util.ItemClassId, true);
        tempItem.setPosition(37.715584, 127.048616);
        tempItem.setName("여기여기 모여라");
        tempItem.setRadius(20);
        _lookChanger.setLook(tempItem.getNetworkId(), ImageType.CIRCLE_WITH_MARKER);

        addNewGameObjectsToWorld();
    }

    @Override
    public void start() {
        CoreHost.getInstance().setMatch(this);
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

        addNewGameObjectsToWorld();
        killGameObjects();

        _collider.update(ms);
        _currentState.update(ms);
    }

    private void killGameObjects(){
        int goSize = _gameObjects.size();
        for (int i = 0; i < goSize; i++) {
            GameObject gameObject = _gameObjects.get(i);
            if (gameObject.wantsToDie()) {
                _worldSetter.generateDestroyInstruction(gameObject.getNetworkId());
                gameObject.faceDeath();
                _gameObjects.set(gameObject.getIndexInWorld(), _gameObjects.get(_gameObjects.size() - 1));
                _gameObjects.get(gameObject.getIndexInWorld()).setIndexInWorld(gameObject.getIndexInWorld());
                _gameObjects.remove(_gameObjects.size() - 1);
                goSize--;
                i--;
            }
        }
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


    public boolean isWorldSetterActive() {
        return _worldSetterActive;
    }

    public void setWorldSetterActive() {
        _worldSetterActive = true;
    }

    public double[] getBattleGroundLatLon() {
        return _battleGroundLatLon;
    }

    public void setBattleGroundLatLon(double lat, double lon) {
        _battleGroundLatLon[0] = lat;
        _battleGroundLatLon[1] = lon;
        _parent.getConverter().setOffset(lat, lon);
    }

    @Override
    public List<PlayerCommon> getPlayers() { return _players; }

    @Override
    public Collider getCollider(){ return _collider; }

    @Override
    public WorldSetterHost getWorldSetterHost() {
        return _worldSetter;
    }

    @Override
    public LatLonByteConverter getConverter(){ return _parent.getConverter(); }

    @Override
    public GameObjectRegistry getRegistry(){ return _registry; }

    @Override
    public List<GameObject> getWorld() {
        return _gameObjects;
    }
}
