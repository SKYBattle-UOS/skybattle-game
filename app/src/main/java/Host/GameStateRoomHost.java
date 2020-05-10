package Host;

import java.io.IOException;

import Common.GameState;
import Common.GameStateType;
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
        NetworkManager net = CoreHost.getInstance().getNetworkManager();
        InputBitStream packetStream = net.getHostClientProxy().getRawPacketQueue().poll();
        if (packetStream != null){
            packetStream.read(_buffer, 8);

            // 방장이 보냄
            if (_buffer[0] == 'a'){
                net.closeAccept();

                try {
                    net.getPacketToSend().write(1, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                _parent.switchState(GameStateType.MATCH);
            }
        }
    }
}
