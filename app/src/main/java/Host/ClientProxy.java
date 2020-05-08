package Host;

import Common.BitInputStream;
import Common.InputBitStream;
import Common.MoveList;

public class ClientProxy {
    private WorldSetterHost _worldSetter;
    private MoveList _unprocessedMoves;
    private int _playerId;
    private boolean _shouldSendLastTimeStamp;
    private InputBitStream _packetStream;

    public ClientProxy(int playerId){
        _playerId = playerId;
        _shouldSendLastTimeStamp = false;
        _worldSetter = new WorldSetterHost();
        _unprocessedMoves = new MoveList();

        // TODO: set size to 0
        _packetStream = new BitInputStream();
    }

    public int getPlayerId(){
        return _playerId;
    }

    public MoveList getUnprocessedMoves(){
        return _unprocessedMoves;
    }

    public byte[] getPacketBuffer(){
        return _packetStream.getBuffer();
    }

    public void setPacketBufferLen(int numBytes){
        _packetStream.resetPos();
        _packetStream.setBufferLength(numBytes);
    }

    public InputBitStream getPacketStream(){
        return _packetStream;
    }
}