package Host;

import android.util.Log;

import java.io.IOException;
import java.util.Collection;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;

public class GameStateRoomHost implements GameState {
    private GameStateContextHost _parent;

    public GameStateRoomHost(GameStateContextHost parent){
        _parent = parent;
    }


    @Override
    public void update(long ms) {
        NetworkManager net = CoreHost.getInstance().getNetworkManager();
        Collection<ClientProxy> players = CoreHost.getInstance().getNetworkManager().
                                            getClientProxies();

        /*
         *  메시지 전송 포맷:
         *      플래그 비트 패턴:
         *          startGame
         *          aPlayerExited
         *          aPlayerChangedTeam
         *          aPlayerChangedName
         *      내용 비트 패턴:
         *          playerId (3비트)
         *          playerTeam (1비트)
         *
         *      해당사항 없는 필드는 생략됨
         *
         */

        boolean playerStartedGame;
        boolean playerExited;
        boolean playerChangedTeam;
        boolean playerChangedName;

        // 메시지 읽기 및 처리
        for (ClientProxy player : players){
            InputBitStream in = player.getRawPacketQueue().poll();
            int message = -1;
            int numMsgBits = 0;

            if (in != null) {
                // 플래그 비트 패턴 읽기
                playerStartedGame = in.read(1) == 1;
                playerExited = in.read(1) == 1;
                playerChangedTeam = in.read(1) == 1;
                playerChangedName = in.read(1) == 1;

                // 전송할 메시지 처리

                // 1. 플래그 비트 추가

                //      게임 시작 비트
                boolean startGame = false;
                if (playerStartedGame) {
                    if (in.read(1) == 1) {      // if (thisPlayerIsHost == 1)
                        // TODO: 플레이어들의 팀 정보 체크
                        startGame = true;
                    }
                }
                message = startGame? 1 : 0;
                numMsgBits++;

                //      나머지
                appendFlagBit(message, playerExited? 1 : 0);
                appendFlagBit(message, playerChangedTeam? 1 : 0);
                appendFlagBit(message, playerChangedName? 1 : 0);
                numMsgBits += 3;


                // 2. 내용 비트 추가

                //      플레이어 식별자
                if (playerExited || playerChangedTeam || playerChangedName) {
                    message = appendMsg(message, player.getPlayerId());
                    numMsgBits += 3;
                }

                //      팀 A/B 비트
                if (playerChangedTeam) {
                    int playerTeam = in.read(1);
                    message = appendFlagBit(message, playerTeam);

                    numMsgBits += 1;
                }

                // 2-3. 플레이어 이름 변경 (미구현)
                /*
                if (playerChangedName) {
                    int nameEncoded = 0;
                    int numNameBits;
                    message = appendName(message, nameEncoded);

                    numMsgBits += numNameBits;
                }
                */
            }

            this.broadcast(net, message, numMsgBits);
        }
    }


    // 모든 클라이언트에 메시지를 보내는 내부 메소드
    private void broadcast(NetworkManager net, int message, int numBits) {
        for (ClientProxy client : net.getClientProxies()) {
            OutputBitStream out = net.getPacketToSend();

            // send message
            try {
                out.write(message, numBits);
                net.shouldSendThisFrame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 게임을 시작하고 다음 화면으로 넘어가도록 하는 내부 메소드
    private void startGame(NetworkManager net) {
        Log.i("Stub", "RoomHost: host started a game");

        net.closeAccept();

        broadcast(net, 1, 1); // 게임 시작 == 1
        _parent.switchState(GameStateType.MATCH);
    }

    // 메시지에 3비트 플레이어 식별자를 추가하는 내부 메소드
    private int appendMsg(int msg, int playerId) {
        msg = msg << 3;
        msg += (playerId | 7);
        return msg;
    }

    private int appendFlagBit(int msg, int flag) {
        msg = msg << 1;
        return msg + flag;
    }
}