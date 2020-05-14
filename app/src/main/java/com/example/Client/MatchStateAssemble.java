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
    private GameStateMatch _parent;
    private boolean _isInitialized;
    private boolean sentInitComplete;
    private int _numPlayers;

    public MatchStateAssemble(GameStateMatch parentMatch, int numPlayers) {
        _parent = parentMatch;
        _isInitialized = false;
        sentInitComplete = false;
        _numPlayers = numPlayers;
        Core.getInstance().getUIManager().setText("다른 플레이어를 기다리는중...");
    }

    @Override
    public void update(long ms) {
        InputBitStream packet = Core.getInstance().getPakcetManager().getPacketStream();
        OutputBitStream outPacket = Core.getInstance().getPakcetManager().getPacketToSend();

        if (packet == null) return;

        if (!sentInitComplete) {
            Collection<GameObject> gos = _parent.getGameObjects();
            if (gos.size() >= _numPlayers) {
                sentInitComplete = true;
                Core.getInstance().getPakcetManager().shouldSendThisFrame();
            }

            Util.sendHas(outPacket, sentInitComplete);
        }
        else
            Util.sendHas(outPacket, sentInitComplete);

        if (Util.hasMessage(packet)) {
            _isInitialized = true;
            Core.getInstance().getUIManager().setText("집합하세요");
        }

        if (Util.hasMessage(packet)) {
            _parent.switchState(MatchStateType.SELECT_CHARACTER);
            Core.getInstance().getUIManager().switchScreen(ScreenType.CHARACTERSELECT);
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        if (_isInitialized) {
            Collection<GameObject> gameObjects = _parent.getGameObjects();
            for (GameObject go : gameObjects) {
                go.render(renderer);
            }
        }
    }
}