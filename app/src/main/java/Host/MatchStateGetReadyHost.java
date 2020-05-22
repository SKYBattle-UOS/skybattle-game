package Host;

import java.io.IOException;

import Common.GameState;
import Common.MatchStateType;
import Common.OutputBitStream;
import Common.Util;

class MatchStateGetReadyHost implements GameState {
    private GameStateMatchHost _match;
    private int _countMS;
    private int _prevCount;

    public MatchStateGetReadyHost(GameStateMatchHost gameStateMatchHost, int getReadyCount) {
        _match = gameStateMatchHost;
        _countMS = getReadyCount;
        _prevCount = _countMS / 1000;
    }

    @Override
    public void update(long ms) {
        boolean countChanged = false;
        boolean startMatch = false;

        _countMS -= ms;
        if (_countMS / 1000 < _prevCount){
            _prevCount = _countMS / 1000;
            countChanged = true;
        }

        if (_countMS < 0)
            startMatch = true;

        OutputBitStream packet = CoreHost.getInstance().getNetworkManager().getPacketToSend();

        Util.sendHas(packet, countChanged);
        if (countChanged){
            CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();
            try {
                packet.write(_prevCount, 8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Util.sendHas(packet, startMatch);
        if (startMatch){
            CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.INGAME);
        }
    }
}
