package Host;

import android.icu.util.Output;
import android.util.Log;

import com.example.Client.NetworkPacketManager;

import java.io.IOException;
import java.util.Collection;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;

public class GameStateRoomHost implements GameState {
    private final int MSG_TYPE_LEN = 2;
    private GameStateContextHost _parent;

    public GameStateRoomHost(GameStateContextHost parent){
        _parent = parent;
    }

    @Override
    public void update(long ms) {
        NetworkManager net = CoreHost.get().getNetworkManager();
        Collection<ClientProxy> clients = CoreHost.get().getNetworkManager().getClientProxies();

        /*
         *  메시지 전송 포맷
         *      MsgType (2비트) | MsgBody (가변)
         *
         *  MsgType
         *      00: startGame
         *      01: playerLeft
         *      10: playerChangedTeam
         *      11: playerChangedName
         *
         *  MsgBody
         *      if MsgType == 00    없음
         *      if MsgType == 01    없음
         *      if MsgType == 10    playerTeam (1비트)
         *      if MsgType == 11    playerName (?비트 / 미구현)
         *
         *  한 틱당 클라이언트마다 각기 하나의 메시지만 받아서 처리 후 전송
         */

        for (ClientProxy client : clients) {
            InputBitStream in = client.getRawPacketQueue().poll();

            if (in != null) {
                boolean playerStartedGame = in.read(1) == 1;;
                boolean playerLeft = in.read(1) == 1;
                boolean playerChangedTeam = in.read(1) == 1;
                boolean playerChangedName = in.read(1) == 1;

                // 메시지 생성
                int broadMsg = -1;
                int numBodyBits = 0;

                if (playerStartedGame) {
                    broadMsg = 0;
                } else if (playerLeft) {
                    broadMsg = 1;
                } else if (playerChangedTeam) {
                    broadMsg = 2;
                    broadMsg = (broadMsg << 1) + in.read(1); // appending playerTeam
                    numBodyBits += 1;
                } else if (playerChangedName) {
                    broadMsg = 3;
                }

                // 메시지 전송
                if (broadMsg != -1) {
                    OutputBitStream out = CoreHost.get().getNetworkManager().getPacketToSend();
                    try {
                        out.write(broadMsg, MSG_TYPE_LEN + numBodyBits);
                        CoreHost.get().getNetworkManager().shouldSendThisFrame();

                        // 게임을 시작하는 경우 상태 변환
                        if (broadMsg == 0) {
                            switchScreen(net);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void switchScreen(NetworkManager net) {
        Log.i("Stub", "RoomHost: switching screen");
        net.closeAccept();

        try {
            net.getPacketToSend().write(1, 1);
            net.shouldSendThisFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }

        _parent.switchState(GameStateType.MATCH);
    }

    /*
    모든 클라이언트에 메시지를 전송하는 메소드; 현재 구조상 적절하지 않음
    private void broadcast(NetworkManager net, int msg, int numBits) {
        Collection<ClientProxy> clients = CoreHost.get().getNetworkManager().getClientProxies();
        for (ClientProxy client : clients) {
            OutputBitStream out = net.getPacketToSend();
            try {
                out.write(msg, numBits);
            } catch (IOException e) {
                e.printStackTrace();
            }
            net.shouldSendThisFrame();
        }
    }
    */

    private int appendMsgBit(int msg, int appended, int numBits) {
        msg = msg << numBits;
        msg += appended;
        return msg;
    }
}
