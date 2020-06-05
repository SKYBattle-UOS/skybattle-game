package com.example.Client;

import android.util.Log;

import Common.GameObject;
import Common.GameState;
import Common.ReadOnlyList;
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
    private boolean _activatedWorldSetter;
    private boolean _isInitComplete;
    private boolean _waiting = false;

    public MatchStateAssemble(GameStateMatch parentMatch) {
        _match = parentMatch;
        _activatedWorldSetter = false;
        Core.get().getUIManager().setTopText("다른 플레이어를 기다리는중...");
    }

    @Override
    public void update(long ms) {
        InputBitStream packet = Core.get().getPakcetManager().getPacketStream();
        OutputBitStream outPacket = Core.get().getPakcetManager().getPacketToSend();

        Util.sendHas(outPacket, !_isInitComplete);
        if (!_isInitComplete){
            Core.get().getPakcetManager().shouldSendThisFrame();
            _isInitComplete = true;
        }

        if (packet == null) return;

        if (Util.hasMessage(packet)) {
            if (!_activatedWorldSetter){
                _match.activateWorldSetter();
                Core.get().getUIManager().setTopText("집합하세요");
                _match.setBattleGroundLatLon(37.714617, 127.045170);
                Core.get().getCamera().move(37.716140, 127.046620);
                Core.get().getCamera().zoom(17);
                _activatedWorldSetter = true;
            }
        }

        if (Util.hasMessage(packet)) {
            _match.switchState(MatchStateType.SELECT_CHARACTER);
            Core.get().getUIManager().switchScreen(ScreenType.CHARACTERSELECT, null);
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        if (_activatedWorldSetter) {
            ReadOnlyList<GameObject> gameObjects = _match.getWorld();
            for (GameObject go : gameObjects) {
                ((Renderable) go).render(renderer);
            }
        }
    }
}