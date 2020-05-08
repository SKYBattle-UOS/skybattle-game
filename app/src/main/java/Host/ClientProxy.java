package Host;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.InputBitStream;
import Common.MoveList;

public class ClientProxy {
    private WorldSetterHost _worldSetter;
    private MoveList _unprocessedMoves;
    private int _playerId;
    private boolean _shouldSendLastTimeStamp;
    private ConcurrentLinkedQueue<InputBitStream> _rawPackets;
    private LinkedList<InputBitStream> _packets;

    public ClientProxy(int playerId){
        _playerId = playerId;
        _shouldSendLastTimeStamp = false;
        _worldSetter = new WorldSetterHost();
        _unprocessedMoves = new MoveList();
        _rawPackets = new ConcurrentLinkedQueue<>();
        _packets = new LinkedList<>();
    }

    public int getPlayerId(){
        return _playerId;
    }

    public MoveList getUnprocessedMoves(){
        return _unprocessedMoves;
    }

    public Queue<InputBitStream> getRawPacketQueue(){
        return _rawPackets;
    }

    public Queue<InputBitStream> getPacketQueue(){
        return _packets;
    }
}