package Host;

import android.util.Log;

import java.io.IOException;
import java.util.Collection;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.Util;

public class GameStateRoomHost implements GameState {
    private GameStateContextHost _parent;
    private byte[] _buffer;
    private boolean _buttonPressed;

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
        Collection<ClientProxy> clients = CoreHost.getInstance().getNetworkManager().getClientProxies();

        for (ClientProxy client : clients){
            InputBitStream packet = client.getRawPacketQueue().poll();
            if (packet == null)
                continue;

            _buttonPressed = packet.read(8) == 77;
        }

        // host sent start
        if (_buttonPressed){
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
