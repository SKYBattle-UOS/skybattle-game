package Host;

import Common.GameState;
import Common.InputBitStream;

public class GameStateRoomHost implements GameState {
    private GameStateContextHost _parent;
    private byte[] _buffer;

    public GameStateRoomHost(GameStateContextHost parent){
        _parent = parent;
        _buffer = new byte[1];
    }

    @Override
    public void update(long ms) {
        // TODO: thread safety
        NetworkManager net = CoreHost.getInstance().getNetworkManager();
        InputBitStream packetStream = net.getHostClientProxy().getPacketStream();
        packetStream.read(_buffer, 8);

        // 방장이 보냄
        if (_buffer[0] == 'a')
            net.broadCastToClients(_buffer);
    }
}
