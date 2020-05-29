package com.example.Client;

import android.util.Log;

import java.io.IOException;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;

public class GameStateRoom implements GameState {
    private final int MAX_NUM_PLAYERS = 6;
    private GameStateContext _parent;

    private boolean playerIsHost;
    private boolean switchingToNextScreen; // 화면 전환 중 패킷 처리를 멈추기 위한 변수

    private boolean playerStartedGame;
    private boolean playerExited;
    private boolean playerChangedTeam;
    private boolean playerChangedName;

    private int playerTeam; // TODO: UI에서 가져오기
    private String playerName; // TODO: UI에서 가져오기

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
        this.playerIsHost = false;
        this.switchingToNextScreen = false;

        this.playerStartedGame = false;
        this.playerExited = false;
        this.playerChangedTeam = false;
        this.playerChangedName = false;

        this.playerTeam = 0;
        this.playerName = "Amugae";
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

        // TODO: 플레이어 입장 후 어플 동작
        //      1. 방장 여부 체크 -> UI에서 메인에서 방만들기/입장하기에서 넘어가는 액티비티를 다르게 설정
        //      2. 플레이어 디폴트 닉네임 설정 (Optional)
        //      3. 플레이어 리스트에 현재 플레이어 추가

        this.playerIsHost = true; // 일단 방장이라고 가정
    }

    @Override
    public void update(long ms) {
        if (!this.switchingToNextScreen) {
            /*
             *  메시지 전송 포맷:
             *      플래그 비트 (앞부분)
             *          playerStartedGame (1비트)
             *          playerExited (1비트)
             *          playerChangedTeam (1비트)
             *          playerChangedName (1비트)
             * --------------------------------------
             *      내용 비트 (뒷부분)
             *          thisClientIsHost (1비트)      0: false  |   1: true
             *          playerTeam (1비트)            0: A팀    |   1: B팀
             *          playerName (?비트)            한영 닉네임, UTF-8 인코딩  <-- 아직 미구현
             */


            // 메시지 전송
            OutputBitStream out = Core.get().getPakcetManager().getPacketToSend();

            this.writeFlagBits(out);
            this.writeContentBits(out);

            if (this.playerStartedGame || this.playerExited || this.playerChangedTeam
                    || this.playerChangedName) {
                Core.get().getPakcetManager().shouldSendThisFrame();
            }


            // 메시지 수신
            InputBitStream in = Core.get().getPakcetManager().getPacketStream();
            this.processMessage(in); // 이 메소드 내부에서 화면 전환 일어남
        }
    }


    // 플래그 비트 패턴을 쓰기하는 내부 메소드
    private void writeFlagBits(OutputBitStream out) {
        try {

            out.write(playerChangedName ? 1 : 0, 1);
            out.write(playerChangedTeam ? 1 : 0, 1);
            out.write(playerStartedGame ? 1 : 0, 1);
            out.write(playerExited ? 1 : 0, 1);
            playerStartedGame = false;
            playerChangedName = false;
            playerChangedTeam = false;
            playerExited = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 내용 비트 패턴을 쓰기하는 내부 메소드
    private void writeContentBits(OutputBitStream out) {
        // 플레이어 게임 시작
        if (this.playerStartedGame) {
            try {
                out.write(this.playerIsHost? 1 : 0, 1); // 시작 메시지 전송
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 플레이어 팀 변경
        if (this.playerChangedTeam) {
            try {
                out.write((this.playerTeam == 0)? 0 : 1, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 플레이어 이름 변경 (미구현)
        if (this.playerChangedName) {

        }
    }

    // 수신된 메시지를 처리하는 내부 메소드
    private void processMessage(InputBitStream in) {
        boolean hostStartedGame;
        // boolean hostKickedPlayer; // 현재 프로그램 구조상 구현 불가

        // 각 클라이언트에 대한 메시지를 순차적으로 전송받아 처리
        for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
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
        }
    }

    // 화면을 전환하는 내부 함수
    private void switchScreen() {
        Core.get().getInputManager().startSending();
        Log.i("Stub", "GameStateRoom: Start Button Pressed by Host");
        this.switchingToNextScreen = true;
        Core.get().getUIManager().switchScreen(ScreenType.ASSEMBLE, ()->_parent.switchState(GameStateType.MATCH));
    }


    /*

    방제 수정 사용하지 않음; 만약의 경우 UTF-8 인코딩 필요하게 되면 참조

    // reads room title that is encoded in UTF-8
    private String getRoomTitle(InputBitStream packet) {
        byte[] bytesHolder = new byte[MAX_TITLE_LENGTH * 4]; // UTF-8: max 4 bytes per character
        packet.read(bytesHolder, bytesHolder.length);
        return new String(bytesHolder, StandardCharsets.UTF_8);
    }

    // reads players' info as a String that is encoded in UTF-8
    private String getPlayersInfo(InputBitStream packet) {
        // TO-DO: should determine the number of bytes required for each player info
        // assumes 10 bytes for now; might want to include: [player name], [isHost]

        byte[] bytesHolder = new byte[MAX_NUM_PLAYERS * 10]; // UTF-8 Encoding: max 4 bytes
        packet.read(bytesHolder, bytesHolder.length);
        return new String(bytesHolder, StandardCharsets.UTF_8);
    }


     */
}