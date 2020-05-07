package Host;

import Common.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;

public class GameStateMatchHost implements GameState {
    private GameState _currentState;
    private WorldSetterHost _worldSetter;
    private ArrayList<GameObject> _world;

    // TODO
    private int _numPlayers;
    private final int GET_READY_COUNT;
    private final int NUM_PACKET_PER_FRAME;

    public GameStateMatchHost(){
        _worldSetter = new WorldSetterHost();
        _world = new ArrayList<>();

        _numPlayers = CoreHost.getInstance().getNetworkManager().getNumConnections();
        GET_READY_COUNT = 10000;
        NUM_PACKET_PER_FRAME = 3;

        // TODO
        switchState(MatchStateType.ASSEMBLE);
    }

    @Override
    public void update(long ms) {
        handleInputFromClients();

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
                handleInputPacket(packet);
            }
        }
    }

    private void handleInputPacket(InputBitStream packet) {

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
}
