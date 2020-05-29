package com.example.Client;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;

public class GameStateRoom implements GameState {
    private final int MAX_TITLE_LENGTH = 10;
    private final int MAX_NUM_PLAYERS = 6;
    private GameStateContext _parent;
    private boolean _waiting = false;
    private boolean _buttonPressed;

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
    }

    @Override
    public void start() {
        Core.getInstance().getUIManager().registerCallback(AndroidUIManager.ROOM_START_PORT,
                ()-> _buttonPressed = true
        );

        // TODO: close connection
        Core.getInstance().getUIManager().registerCallback(AndroidUIManager.EXIT_ROOM_PORT,
                ()-> Core.getInstance().getUIManager().switchScreen(ScreenType.MAIN,
                        ()->_parent.switchState(GameStateType.MAIN))
        );
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;

        OutputBitStream outPacket = Core.getInstance().getPakcetManager().getPacketToSend();

        if (_buttonPressed){
            try {
                outPacket.write(1, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Core.getInstance().getPakcetManager().shouldSendThisFrame();
            _buttonPressed = false;
        }

        // TODO: might be better if packet is fetched only once
        InputBitStream packet = Core.getInstance().getPakcetManager().getPacketStream();
        if (packet == null) return;

//        if (roomTitleChanged(packet)) {
//            Log.i("Stub", "GameStateRoom: Room Title Changed - " +
//                    getRoomTitle(packet));
//        }
//
//        if (playersInfoChanged(packet)) {
//            Log.i("Stub", "GameStateRoom: Room Title Changed - " +
//                    getPlayersInfo(packet));
//        }

        if (hostStartedGame(packet)) {
            // assemble
//            if (isAbleToStart(packet)) {
            Core.getInstance().getInputManager().startSending();
            Log.i("Stub", "GameStateRoom: Start Button Pressed by Host");
            _waiting = true;
            Core.getInstance().getUIManager().switchScreen(ScreenType.MAP, ()->_parent.switchState(GameStateType.MATCH));
//            }
        }
    }

    // returns if the room title has changed
    private boolean roomTitleChanged(InputBitStream packet) {
        return packet.read(1) == 1;
    }

    private boolean playersInfoChanged(InputBitStream packet) {
        return packet.read(1) == 1;
    }

    // returns if room host started game
    private boolean hostStartedGame(InputBitStream packet) {
        return packet.read(1) == 1;
    }

    // returns if game is able to start; checks the number of players
    private boolean isAbleToStart(InputBitStream packet) {
        /*
         * 'packet received' semantics (in binary):
         *      00 == not enough players && players in one place
         *      01 == not enough players && players in one place
         *      10 == enough players && players not in one place
         *      11 == enough players && players not in one place
         */
        return packet.read(2) == 3;
    }

    // reads room title that is encoded in UTF-8
    private String getRoomTitle(InputBitStream packet) {
        byte[] bytesHolder = new byte[MAX_TITLE_LENGTH * 4]; // UTF-8: max 4 bytes per character
        packet.read(bytesHolder, bytesHolder.length);
        return new String(bytesHolder, StandardCharsets.UTF_8);
    }

    // reads players' info as a String that is encoded in UTF-8
    private String getPlayersInfo(InputBitStream packet) {
        // TODO: should determine the number of bytes required for each player info
        // assumes 10 bytes for now; might want to include: [player name], [isHost]

        byte[] bytesHolder = new byte[MAX_NUM_PLAYERS * 10]; // UTF-8 Encoding: max 4 bytes
        packet.read(bytesHolder, bytesHolder.length);
        return new String(bytesHolder, StandardCharsets.UTF_8);
    }
}