package Host;

import com.example.Client.Core;
import com.example.Client.GameObjectFactory;
import com.example.Client.GameObjectRegistry;
import Common.ImageType;

import Common.Collider;
import Common.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import Common.GameState;
import Common.InputBitStream;
import Common.InputState;
import Common.LatLonByteConverter;
import Common.MatchStateType;
import Common.Player;
import Common.PlayerHost;
import Common.ReadOnlyList;
import Common.RoomUserInfo;
import Common.TimerStruct;
import Common.Util;

public class GameStateMatchHost implements GameState, MatchHost {
    private GameStateContextHost _parent;
    private GameState _currentState;
    private WorldSetterHost _worldSetter;
    private ArrayList<GameObject> _gameObjects;
    private ArrayList<GameObject> _newGameObjects;
    private ArrayList<Integer> _newGOClassId;
    private ArrayList<Player> _players;
    private GameObjectRegistry _registry;
    private GameObjectFactory _factory;
    private Collider _collider;
    private int _nextNetworkId = 1;
    private boolean _worldSetterActive = false;
    private PriorityQueue<TimerStruct> _timerQueue = new PriorityQueue<>();

    private ReadOnlyList<GameObject> _readOnlyGameObjects;
    private ReadOnlyList<Player> _readOnlyPlayers;

    // TODO
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

        _readOnlyGameObjects = new ReadOnlyList<>(_gameObjects);
        _readOnlyPlayers = new ReadOnlyList<>(_players);

        _battleGroundLatLon = new double[2];
        GET_READY_COUNT = 3000;
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

    @Override
    public void setTimer(Runnable callback, float seconds) {
        long timeToBeFired = Core.get().getTime().getStartOfFrame();
        timeToBeFired += (long) seconds * 1000;
        _timerQueue.add(new TimerStruct(callback, timeToBeFired));
    }

    private void addNewGameObjectsToWorld(){
        int i = 0;
        for (GameObject go : _newGameObjects){
            _registry.add(go.getNetworkId(), go);
            go.setIndexInWorld(_gameObjects.size());
            _gameObjects.add(go);
            _worldSetter.generateCreateInstruction(_newGOClassId.get(i++), go.getNetworkId(), -1);

            if (go instanceof Player)
                _players.add((Player) go);
        }

        _newGameObjects.clear();
        _newGOClassId.clear();
    }

    public void createPlayers() {
        int lastPlayerId = 0;
        for (RoomUserInfo info : _parent.getUsers().values()){
            PlayerHost newPlayer = (PlayerHost) createGameObject(Util.PlayerClassId, true);
            newPlayer.getProperty().setPlayerId(info.playerId);
            lastPlayerId = info.playerId;
            newPlayer.setPosition(37.716140, 127.046620);
            newPlayer.setName(info.name);
            newPlayer.getProperty().setTeam(info.team);
            newPlayer.setLook(ImageType.MARKER);
        }

        DummyPlayerHost dummy = (DummyPlayerHost) createGameObject(Util.DummyPlayerClassId, true);
        dummy.getProperty().setPlayerId(lastPlayerId + 1);
        dummy.setPosition(37.716109 - 0.0005, 127.048926 - 0.0005);
        dummy.setName("테스트용 팀 A");
        dummy.setLook(ImageType.MARKER);
        dummy.getProperty().setTeam(0);

        DummyPlayerHost dummy2 = (DummyPlayerHost) createGameObject(Util.DummyPlayerClassId, true);
        dummy2.getProperty().setPlayerId(lastPlayerId + 2);
        dummy2.setPosition(37.716109 - 0.0005, 127.048926 + 0.0005);
        dummy2.setName("테스트용 팀 B");
        dummy2.setLook(ImageType.MARKER);
        dummy2.getProperty().setTeam(1);


        // create temp item
        GameObject assemblePoint = createGameObject(Util.ItemClassId, true);
        assemblePoint.setPosition(37.716109, 127.048926);
        assemblePoint.setName("여기여기 모여라");
        assemblePoint.setRadius(100);
        assemblePoint.setLook(ImageType.CIRCLE_WITH_MARKER);

        GameObject respawnPoint = createGameObject(Util.ItemClassId, true);
        respawnPoint.setPosition(37.715151, 127.045780);
        respawnPoint.setName("부활지점");
        respawnPoint.setRadius(20);
        respawnPoint.setLook(ImageType.INVISIBLE);

        addNewGameObjectsToWorld();
    }

    @Override
    public void start() {
        CoreHost.get().setMatch(this);
    }

    @Override
    public void update(long ms) {
        for (GameObject go : _gameObjects)
            go.before(ms);

        handleInputFromClients();

        for (GameObject go : _gameObjects)
            go.update(ms);

        addNewGameObjectsToWorld();
        killGameObjects();

        if (_worldSetterActive)
            _worldSetter.writeInstructionToStream(CoreHost.get().getNetworkManager().getPacketToSend());

        for (GameObject go : _gameObjects)
            go.after(ms);

        processTimers();

        _collider.update(ms);
        _currentState.update(ms);
    }

    private void processTimers() {
        while (true){
            TimerStruct ts = _timerQueue.peek();
            if (ts == null) return;

            if (ts.timeToBeFired < Core.get().getTime().getStartOfFrame()){
                ts.callback.run();
                _timerQueue.poll();
            }
            else
                return;
        }
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
        NetworkManager net = CoreHost.get().getNetworkManager();
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
                _currentState = new MatchStateAssembleHost(this);
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
    public ReadOnlyList<Player> getPlayers() { return _readOnlyPlayers; }

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
    public ReadOnlyList<GameObject> getWorld() {
        return _readOnlyGameObjects;
    }
}
