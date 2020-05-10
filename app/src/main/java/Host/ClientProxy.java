package Host;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.InputBitStream;
import Common.MoveList;

public class ClientProxy {
    private MoveList _unprocessedMoves;
    private int _playerId;
    private boolean _shouldSendLastTimeStamp;
    private ConcurrentLinkedQueue<InputBitStream> _rawPackets;
    private LinkedList<InputBitStream> _packets;
    private boolean _isDisconnected;

    public ClientProxy(int playerId){
        _playerId = playerId;
        _shouldSendLastTimeStamp = false;
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

    public void setDisconnected(boolean disconnected){
        _isDisconnected = disconnected;
    }

    public boolean isDisconnected(){
        return _isDisconnected;
    }
}