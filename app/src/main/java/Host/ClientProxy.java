package Host;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.InputBitStream;
import Common.InputState;

public class ClientProxy {
    private Queue<InputState> _unprocessedInputs;
    private int _playerId;
    private boolean _shouldSendLastTimeStamp;
    private ConcurrentLinkedQueue<InputBitStream> _rawPackets;
    private LinkedList<InputBitStream> _packets;
    private boolean _isDisconnected;
    private WorldSetterHost _worldSetter;

    public ClientProxy(int playerId){
        _playerId = playerId;
        _shouldSendLastTimeStamp = false;
        _unprocessedInputs = new LinkedList<>();
        _rawPackets = new ConcurrentLinkedQueue<>();
        _packets = new LinkedList<>();
        _worldSetter = new WorldSetterHost();
    }

    public int getPlayerId(){
        return _playerId;
    }

    public Queue<InputState> getUnprocessedInputs(){
        return _unprocessedInputs;
    }

    public Queue<InputBitStream> getRawPacketQueue(){
        return _rawPackets;
    }

    public Queue<InputBitStream> getPacketQueue(){
        return _packets;
    }

    public void setDisconnected(boolean disconnected){
        _isDisconnected = disconnected;
    }

    public boolean isDisconnected(){
        return _isDisconnected;
    }

    public WorldSetterHost getWorldSetterHost(){
        return _worldSetter;
    }
}