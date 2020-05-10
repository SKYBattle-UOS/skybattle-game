package com.example.Client;

import java.io.IOException;
import java.util.Collection;

import Common.GameObject;
import Common.GameState;
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
    private boolean _sentConfirm;
    private int _numPlayers;

    public MatchStateAssemble(GameStateMatch parentMatch, int numPlayers) {
        _parent = parentMatch;
        _isInitialized = false;
        _sentConfirm = false;
        _numPlayers = numPlayers;
        Core.getInstance().getUIManager().setText("다른 플레이어를 기다리는중...");
    }

    @Override
    public void update(long ms) {
        InputBitStream packet = Core.getInstance().getPakcetManager().getPacketStream();

        if (!_sentConfirm){
            Collection<GameObject> gos = _parent.getGameObjects();
            if (gos.size() >= _numPlayers){
                _sentConfirm = true;
            }
            confirmPlayerInit(_sentConfirm);
        }

        if (!hasCustomMessage(packet)) return;

        if (isEverybodyInitializedForAssemble(packet)){
            _isInitialized = true;
            Core.getInstance().getUIManager().setText("집합하세요");
        }

        if (isAssembleComplete(packet)){
            _parent.switchState(MatchStateType.SELECT_CHARACTER);
            Core.getInstance().getUIManager().switchScreen(ScreenType.CHARACTERSELECT);
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        if (_isInitialized) {
            Collection<GameObject> gameObjects = _parent.getGameObjects();
            for (GameObject go : gameObjects){
                go.render(renderer);
            }
        }
    }

    private boolean hasCustomMessage(InputBitStream packet) {
        return packet.read(1) == 1;
    }

    private void confirmPlayerInit(boolean confirm){
        OutputBitStream packet = Core.getInstance().getPakcetManager().getPacketToSend();
        int data = confirm ? 1 : 0;
        try {
            packet.write(data, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  boolean isEverybodyInitializedForAssemble(InputBitStream packet){
        return packet.read(1) == 0;
    }

    private boolean isAssembleComplete(InputBitStream packet){
        return packet.read(1) == 1;
    }
}