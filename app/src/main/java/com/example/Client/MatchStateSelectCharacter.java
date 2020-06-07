package com.example.Client;

import java.io.IOException;

import Common.CharacterFactory;
import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;
import Common.Player;
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
    private boolean _waiting;
    private int _selectedCharacter = -1;
    private boolean _sentCharacter;

    MatchStateSelectCharacter(GameStateMatch match) {
        _match = match;
        Core.get().getUIManager().setTopText("집합 완료 : 캐릭터를 선택하세요");
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;

        receive();
        send();
    }

    public void selectCharacter(int index){
        _selectedCharacter = index;
    }

    private void send() {
        OutputBitStream packetToSend = Core.get().getPakcetManager().getPacketToSend();
        boolean shouldSend = (_selectedCharacter >= 0 && !_sentCharacter);
        Util.sendHas(packetToSend, shouldSend);
        if (shouldSend){
            try {
                packetToSend.write(_selectedCharacter, 8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Core.get().getPakcetManager().shouldSendThisFrame();
            _sentCharacter = true;
        }
    }

    private void receive() {
        InputBitStream packet = Core.get().getPakcetManager().getPacketStream();
        if (packet == null) return;

        // character select complete
        if (Util.hasMessage(packet)){
            int numPlayers = packet.read(8);
            for (int i = 0; i < numPlayers; i++){
                int playerId = packet.read(32);
                int character = packet.read(8);
                Player player = findPlayer(playerId);
                _match.getCharacterFactory().setCharacterProperty(player, character);
            }

            _waiting = true;
            Core.get().getUIManager().switchScreen(ScreenType.MAP, ()->_match.switchState(MatchStateType.GET_READY));
        }
    }

    private Player findPlayer(int playerId) {
        for (Player p : _match.getPlayers())
            if (p.getProperty().getPlayerId() == playerId)
                return p;
        return null;
    }
}
