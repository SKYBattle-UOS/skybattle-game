package com.example.Client;

import java.io.IOException;

import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;
import Common.Util;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 캐릭터 선택 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateSelectCharacter implements GameState {
    private GameStateMatch _match;
    private boolean _selectedCharacter;
    private boolean _sentSelected;
    private boolean _waiting;

    MatchStateSelectCharacter(GameStateMatch match){
        _match = match;
        Core.getInstance().getUIManager().setTopText("집합 완료 : 캐릭터를 선택하세요");
        _selectedCharacter = false;
        _sentSelected = false;
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;

        InputBitStream packet = Core.getInstance().getPakcetManager().getPacketStream();
        if (packet == null) return;

        if (_selectedCharacter && !_sentSelected){
            Core.getInstance().getPakcetManager().shouldSendThisFrame();
            _sentSelected = true;
            return;
        }

        OutputBitStream packetToSend = Core.getInstance().getPakcetManager().getPacketToSend();
        Util.sendHas(packetToSend, _selectedCharacter);

        if (Util.hasMessage(packet)){
            _waiting = true;
            Core.getInstance().getUIManager().switchScreen(ScreenType.GETREADY, ()->_match.switchState(MatchStateType.GET_READY));
        }
    }
}
