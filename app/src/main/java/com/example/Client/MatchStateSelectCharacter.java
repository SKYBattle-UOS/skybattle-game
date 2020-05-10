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
    private GameStateMatch _match;
    private IOManager _ioManager;

    MatchStateSelectCharacter(GameStateMatch match){
        _match = match;
        _ioManager = Core.getInstance().getIOManager();
        Core.getInstance().getUIManager().setText("집합 완료 : 캐릭터를 선택하세요");
    }

    @Override
    public void update(long ms) {
        if (_ioManager.isCharacterSelectComplete()){
            Core.getInstance().getUIManager().switchScreen(ScreenType.GETREADY);
            _match.switchState(MatchStateType.GET_READY);
        }
    }
}
