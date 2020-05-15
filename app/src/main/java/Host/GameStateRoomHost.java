package Host;

import android.util.Log;

import java.io.IOException;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;

public class GameStateRoomHost implements GameState {
    private GameStateContextHost _parent;
    private byte[] _buffer;

    // TODO
    private long _elapsed;

    public GameStateRoomHost(GameStateContextHost parent){
        _parent = parent;
        _buffer = new byte[1];
    }

    @Override
    public void update(long ms) {
        _elapsed += ms;
        NetworkManager net = CoreHost.getInstance().getNetworkManager();

        // host sent start
        if (_elapsed > 5000){
            Log.i("Stub", "RoomHost: start button press received");
            net.closeAccept();

            try {
                net.getPacketToSend().write(1, 1);
                net.shouldSendThisFrame();
            } catch (IOException e) {
                e.printStackTrace();
            }

            _parent.switchState(GameStateType.MATCH);
        }
    }
}
