package com.example.Client;

import java.util.Collection;

import Common.GameObject;
import Common.GameState;
import Common.Util;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.OutputBitStream;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 집합화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateAssemble implements GameState {
    private GameStateMatch _match;
    private boolean _isInitialized;
    private boolean sentInitComplete;
    private int _numPlayers;
    private boolean _waiting = false;

    public MatchStateAssemble(GameStateMatch parentMatch, int numPlayers) {
        _match = parentMatch;
        _isInitialized = false;
        sentInitComplete = false;
        _numPlayers = numPlayers;
        Core.getInstance().getUIManager().setTopText("다른 플레이어를 기다리는중...");
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;

        InputBitStream packet = Core.getInstance().getPakcetManager().getPacketStream();
        OutputBitStream outPacket = Core.getInstance().getPakcetManager().getPacketToSend();

        if (!sentInitComplete) {
//            Collection<GameObject> gos = _parent.getGameObjects();
//            if (gos.size() >= _numPlayers) {
                sentInitComplete = true;
                Core.getInstance().getPakcetManager().shouldSendThisFrame();
//            }

            Util.sendHas(outPacket, sentInitComplete);
        }
        else
            Util.sendHas(outPacket, sentInitComplete);

        if (packet == null) return;

        if (Util.hasMessage(packet)) {
            _match.activateWorldSetter();
            _isInitialized = true;
            Core.getInstance().getUIManager().setTopText("집합하세요");
        }

        if (Util.hasMessage(packet)) {
            _waiting = true;
            Core.getInstance().getUIManager().switchScreen(ScreenType.CHARACTERSELECT, ()-> _match.switchState(MatchStateType.SELECT_CHARACTER));
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        if (_isInitialized) {
            Collection<GameObject> gameObjects = _match.getGameObjects();
            for (GameObject go : gameObjects) {
                go.render(renderer);
            }
        }
    }
}