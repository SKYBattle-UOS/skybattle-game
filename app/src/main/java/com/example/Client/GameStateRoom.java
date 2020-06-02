package com.example.Client;

import android.util.Log;

import java.io.IOException;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;

public class GameStateRoom implements GameState {
    private final int MAX_NUM_PLAYERS = 6;
    private final int MSG_TYPE_LEN = 2;
    private GameStateContext _parent;
    private boolean switchingToNextScreen; // 화면 전환 중 패킷 처리를 멈추기 위한 상태 체크 변수

    private boolean playerIsHost;
    private int playerTeam; // TODO: UI에서 가져오기
    private String playerName; // TODO: UI에서 가져오기

    private boolean playerStartedGame;
    private boolean playerLeft;
    private boolean playerChangedTeam;
    private boolean playerChangedName;

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
        this.switchingToNextScreen = false;

        this.playerIsHost = false;
        this.playerTeam = 0; // 0: team A    1: team B
        this.playerName = "Amugae";

        this.playerStartedGame = false;
        this.playerLeft = false;
        this.playerChangedTeam = false;
        this.playerChangedName = false;


    }


    @Override
    public void start() {
        // 메소드 함수 등록 (시작하기 / 나가기 기능 등록)
        Core.get().getUIManager().registerCallback(AndroidUIManager.ROOM_START_PORT,
                ()-> this.playerStartedGame = true
        );

        // TODO: close connection
        Core.get().getUIManager().registerCallback(AndroidUIManager.EXIT_ROOM_PORT,
                ()-> Core.get().getUIManager().switchScreen(ScreenType.MAIN,
                        ()->_parent.switchState(GameStateType.MAIN))
        );
    }

    @Override
    public void update(long ms) {
        if (!this.switchingToNextScreen) {
            /*
             *  메시지 전송 포맷
             *      MsgType (2비트) | MsgBody (가변)
             *
             *  MsgType
             *      00: playerStartedGame
             *      01: playerLeft
             *      10: playerChangedTeam
             *      11: playerChangedName
             *
             *  MsgBody
             *      if MsgType == 00    playerIsHost (1비트)
             *      if MsgType == 01    playerTeam (1비트)
             *      if MsgType == 10    없음
             *      if MsgType == 11    playerName (미구현)
             *
             *  한 틱당 하나의 메시지만 전송
             */
            this.sendMsg();
            this.rcvMsg();
        }
    }

    public void setPlayerIsHost() {
        this.playerIsHost = true;
    }

    public void setPlayerLeft() {
        this.playerLeft = true;
    }

    public void setPlayerChangedTeam(int playerTeam) {
        this.playerChangedTeam = true;
        this.playerTeam = playerTeam;
    }

    public void setPlayerChangedName(String playerName) {
        this.playerChangedName = true;
        // this.playerName = playerName;
    }


    private void sendMsg() {
        OutputBitStream out = Core.get().getPakcetManager().getPacketToSend();

        // 메시지 생성
        int msgType = -1;
        int msgBody = -1;
        int numBodyBits = 0;

        if (this.playerStartedGame) {
            msgType = 0;
            msgBody = this.playerIsHost? 1 : 0;
            numBodyBits = 1;

            this.playerStartedGame = false;
        } else if (this.playerLeft) {
            msgType = 1;

            this.playerLeft = false;
        } else if (this.playerChangedTeam) {
            msgType = 2;
            msgBody = playerTeam;
            numBodyBits = 1;

            this.playerChangedTeam = false;
        } else if (this.playerChangedName) {
            msgType = 3;
            // 이름 수정 처리 미구현

            this.playerChangedName = false;
        }

        // 메시지 존재하면 전송
        if (msgType != -1) {
            int msg = combineMsg(msgType, msgBody, numBodyBits);
            try {
                out.write(msg, MSG_TYPE_LEN + numBodyBits);
                Core.get().getPakcetManager().shouldSendThisFrame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 수신된 메시지를 처리하는 내부 메소드
    private void rcvMsg() {
        InputBitStream in = Core.get().getPakcetManager().getPacketStream();

        // 각 클라이언트에 대한 메시지를 순차적으로 전송받아 처리 --> 현재 프로그램 구조에 맞지 않음)
        // for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
            if (in == null) {
                return;
            }

            // 메시지 처리
            boolean startGame = in.read(1) == 1;
            boolean somePlayerExited = in.read(1) == 1;
            boolean somePlayerChangedTeam = in.read(1) == 1;
            boolean somePlayerChangedName = in.read(1) == 1;

            if (startGame) {
                switchScreen();
            }

            int somePlayerId;
            if (somePlayerExited || somePlayerChangedTeam || somePlayerChangedName) {
                somePlayerId = in.read(3);
            }

            if (somePlayerExited) {
                // TODO: 플레이어 퇴장 후 화면 갱신
            }

            if (somePlayerChangedTeam) {
                int somePlayerTeam = in.read(1);
                // TODO: 변경된 팀 화면 표시
            }

            if (somePlayerChangedName) {
                // TODO: 변경된 이름 화면 표시 (미구현)
            }
        //}
    }

    // 메시지 타입과 메시지 내용을 받아서 메시지 형태로 반환하는 내부 메소드
    private int combineMsg(int msgType, int msgBody, int numBodyBits) {
        if (numBodyBits > 0) {
            return (msgType << numBodyBits) + msgBody;
        } else {
            return msgType;
        }
    }

    // 화면을 전환하는 내부 함수
    private void switchScreen() {
        Core.get().getInputManager().startSending();
        Log.i("Stub", "GameStateRoom: Start Button Pressed by Host");
        this.switchingToNextScreen = true;
        Core.get().getUIManager().switchScreen(ScreenType.MAP, ()->_parent.switchState(GameStateType.MATCH));
    }
}