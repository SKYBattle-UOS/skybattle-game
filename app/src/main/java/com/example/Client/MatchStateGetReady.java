package com.example.Client;

import java.util.Collection;
import java.util.Locale;

import Common.GameObject;
import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.Util;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 준비화면.
 * 게임시작 전 러너들이 도망가는 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21a
 */
public class MatchStateGetReady implements GameState {
    private final String TOP_TEXT = "도망가세요. 남은시간 : %d초";
    private int _count;
    private int _prevCount;
    private GameStateMatch _match;

    MatchStateGetReady(GameStateMatch parent){
        _match = parent;
        Core.get().getUIManager().setTopText(String.format(Locale.getDefault(), TOP_TEXT, _prevCount));
    }

    @Override
    public void update(long ms) {
        InputBitStream packet = Core.get().getPakcetManager().getPacketStream();
        if (packet == null) return;

        if (Util.hasMessage(packet)){
            _count = packet.read(8);
        }

        if (Util.hasMessage(packet)){
            _match.switchState(MatchStateType.INGAME);
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        if (_prevCount != _count){
            _prevCount = _count;
            Core.get().getUIManager().setTopText(String.format(Locale.getDefault(), TOP_TEXT, _count));
        }

        Collection<GameObject> gameObjects = _match.getWorld();
        for (GameObject go : gameObjects){
            ((GameObjectClient) go).render(renderer);
        }
    }
}
