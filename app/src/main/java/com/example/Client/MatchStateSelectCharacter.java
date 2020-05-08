package com.example.Client;

import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 캐릭터 선택 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateSelectCharacter implements GameState {
    private byte[] _buffer = new byte[1];
    private GameStateMatch _match;

    MatchStateSelectCharacter(GameStateMatch match){
        _match = match;
    }

    @Override
    public void update(long ms) {
        InputBitStream packetStream = Core.getInstance().getInstructionManager().getPacketStream();
        if (packetStream == null) return;
        packetStream.read(_buffer, 8);

        if (_buffer[0] == 'c'){
            Core.getInstance().getUIManager().switchScreen(ScreenType.GETREADY);
            _match.switchState(MatchStateType.GET_READY);
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
//        Log.i("Stub", "MatchStateSelectCharacter: Showing Character Selection Screen");
    }
}
