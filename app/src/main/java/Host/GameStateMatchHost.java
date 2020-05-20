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
    private GameState _currentState;
    private WorldSetterHost _worldSetter;
    private ArrayList<GameObjectHost> _gameObjects;
    private ArrayList<PlayerHost> _players;
    private GameObjectRegistry _registry;
    private GameObjectFactory _factory;
    private Collider _collider;
    private int nextNetworkId;
    private boolean _worldSetterActive = false;

    // TODO
    private int _numPlayers;
    private final int GET_READY_COUNT;
    private final int NUM_PACKET_PER_FRAME;

    public GameStateMatchHost(){
        _registry = new GameObjectRegistry();
        _worldSetter = new WorldSetterHost(_registry);
        _factory = new GameObjectFactory();
        _gameObjects = new ArrayList<>();
        _players = new ArrayList<>();
        _collider = new Collider();

        _numPlayers = CoreHost.getInstance().getNetworkManager().getNumConnections();
        GET_READY_COUNT = 10000;
        NUM_PACKET_PER_FRAME = 3;

        // TODO
        Util.registerGameObjectsHost(_factory);
        switchState(MatchStateType.ASSEMBLE);
    }

    public GameObjectHost createGameObject(int classId){
        GameObjectHost ret = (GameObjectHost) _factory.createGameObject(classId);
        int networkId = nextNetworkId++;

        ret.setNetworkId(networkId);
        ret.setWorldSetterHost(_worldSetter);
        ret.setIndexInWorld(_gameObjects.size());
        ret.setCollider(_collider);

        _registry.add(networkId, ret);
        _gameObjects.add(ret);
        _worldSetter.generateCreateInstruction(classId, networkId, -1);

        return ret;
    }

    public void createTempPlayers() {
        Collection<ClientProxy> clients = CoreHost.getInstance().getNetworkManager().getClientProxies();
        for (ClientProxy client : clients){
            int networkId = nextNetworkId++;

            TempPlayerHost newPlayer = (TempPlayerHost) _factory.createGameObject(TempPlayerHost.classId);
            newPlayer.setNetworkId(networkId);
            newPlayer.setPlayerId(client.getPlayerId());
            newPlayer.setWorldSetterHost(_worldSetter);
            newPlayer.setIndexInWorld(_gameObjects.size());
            newPlayer.setCollider(_collider);

            _registry.add(networkId, newPlayer);
            _gameObjects.add(newPlayer);
            _players.add(newPlayer);

            _worldSetter.generateCreateInstruction(TempPlayerHost.classId, networkId, -1);
        }
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

    public Collection<GameObjectHost> getGameObjects(){
        return _gameObjects;
    }
    public Collection<PlayerHost> getPlayers() { return _players; }

    public boolean isWorldSetterActive() {
        return _worldSetterActive;
    }
}
