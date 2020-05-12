package Host;

import com.example.Client.Core;
import com.example.Client.GameObjectRegistry;

import Common.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import Common.GameState;
import Common.InputBitStream;
import Common.InputState;
import Common.MatchStateType;
import Common.TempPlayer;

public class GameStateMatchHost implements GameState {
    private GameState _currentState;
    private WorldSetterHost _worldSetter;
    private ArrayList<GameObject> _gameObjects;
    private GameObjectRegistry _registry;
    private int nextNetworkId;

    // TODO
    private int _numPlayers;
    private final int GET_READY_COUNT;
    private final int NUM_PACKET_PER_FRAME;

    public GameStateMatchHost(){
        _worldSetter = new WorldSetterHost();
        _gameObjects = new ArrayList<>();
        _registry = new GameObjectRegistry();

        _numPlayers = CoreHost.getInstance().getNetworkManager().getNumConnections();
        GET_READY_COUNT = 10000;
        NUM_PACKET_PER_FRAME = 3;

        // TODO
        switchState(MatchStateType.ASSEMBLE);
    }

    @Override
    public void start() {
        createTempPlayers();
    }

    private void createTempPlayers() {
        Collection<ClientProxy> clients = CoreHost.getInstance().getNetworkManager().getClientProxies();
        for (ClientProxy client : clients){
            int networkId = nextNetworkId++;

            TempPlayer newPlayer = (TempPlayer) Core.getInstance().getGameObjectFactory().createGameObject(TempPlayer.classId);
            newPlayer.setNetworkId(networkId);
            newPlayer.setPlayerId(client.getPlayerId());

            _registry.add(networkId, newPlayer);
            _gameObjects.add(newPlayer);

            for (ClientProxy client2 : clients)
                client2.getWorldSetterHost().generateCreateInstruction(networkId, -1);
        }
    }

    @Override
    public void update(long ms) {
        handleInputFromClients();
        _worldSetter.writeInstructionToStream(CoreHost.getInstance().getNetworkManager().getPacketToSend());

        for (GameObject go : _gameObjects){
            go.update(ms);
        }

        _currentState.update(ms);
    }

    private void handleInputFromClients() {
        NetworkManager net = CoreHost.getInstance().getNetworkManager();
        Collection<ClientProxy> clients = net.getClientProxies();

        for (ClientProxy client : clients){
            Queue<InputBitStream> packetQueue = client.getRawPacketQueue();
            for (int i = 0; i < NUM_PACKET_PER_FRAME; i++){
                InputBitStream packet = packetQueue.poll();
                if (packet == null)
                    break;
                handleInputPacket(client, packet);
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
}
