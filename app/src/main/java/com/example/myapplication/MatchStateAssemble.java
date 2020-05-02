package com.example.myapplication;

import android.util.Log;

import java.nio.ByteBuffer;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 집합화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateAssemble implements GameState {
    private GameStateMatch _parent;

    public MatchStateAssemble(GameStateMatch parentMatch) {
        this._parent = parentMatch;
    }

    @Override
    public void update(int ms) {
        InputBitStream packetStream = Core.getInstance().getInstructionManager().getPacketStream();
        if (packetStream == null) return;

        byte[] buffer = new byte[1];
        packetStream.readBytes(buffer, 8);
        int message = buffer[0];

        // match start
        if (message == 's') { // 's' == '집합 완료' 메시지라 가정 (임시)
            _parent.switchState(MatchStateType.GET_READY);
            _parent.createCharacters();
        }

        Log.d("Stub", "MatchStateAssemble: Showing assemble screen");
    }
}