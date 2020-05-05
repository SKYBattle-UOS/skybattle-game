package com.example.Client;

import java.util.Collection;

import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;

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
    private byte[] _buffer;
    private int _numPlayers;

    public MatchStateAssemble(GameStateMatch parentMatch, int numPlayers) {
        _parent = parentMatch;
        _isInitialized = false;
        _sentConfirm = false;
        _buffer = new byte[1];
        _numPlayers = numPlayers;
    }

    @Override
    public void update(long ms) {
        InputBitStream packetStream = Core.getInstance().getInstructionManager().getPacketStream();
        if (packetStream == null) return;
        packetStream.readBytes(_buffer, 8);

        if (!_sentConfirm){
            Collection<GameObject> gos = _parent.getGameObjects();
            if (gos.size() >= _numPlayers){
                Core.getInstance().getInputManager().confirmPlayerInit();
                _sentConfirm = true;
            }
        }

        switch(_buffer[0]){
            // everybody's initialized for assemble
            case 'i':
                _isInitialized = true;
                break;

            // assemble complete
            case 's':
                _parent.switchState(MatchStateType.SELECT_CHARACTER);
                Core.getInstance().getUIManager().switchScreen(ScreenType.CHARACTERSELECT);
                break;
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        if (_isInitialized) {
            Collection<GameObject> gameObjects = _parent.getGameObjects();
            for (GameObject go : gameObjects){
                go.render(renderer, ms);
            }
        }
    }
}