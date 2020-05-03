package com.example.myapplication;

import android.util.Log;

import java.util.Collection;

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
    private byte[] _buffer;
    private int _numPlayers;

    public MatchStateAssemble(GameStateMatch parentMatch, int numPlayers) {
        _parent = parentMatch;
        _isInitialized = false;
        _buffer = new byte[1];
        _numPlayers = numPlayers;
    }

    @Override
    public void update(int ms) {
        InputBitStream packetStream = Core.getInstance().getInstructionManager().getPacketStream();
        if (packetStream == null) return;
        packetStream.readBytes(_buffer, 8);

        if (!_isInitialized){
            // TODO: sending too many
            Collection<GameObject> gos = _parent.getGameObjects();
            if (gos.size() >= _numPlayers)
                Core.getInstance().getInstructionManager().sendInput(new byte[]{'a'});
        }

        if (_buffer[0] == 'i') {
            _isInitialized = true;
        }
        else if (_isInitialized && _buffer[0] == 's') { // 's' == '집합 완료' 메시지라 가정 (임시)
            _parent.switchState(MatchStateType.GET_READY);
        }
    }

    @Override
    public void render(Renderer renderer, int ms) {
        if (!_isInitialized)
            Log.d("Stub", "MatchStateAssemble: Initializing...");
        else {
            Log.d("Stub", "MatchStateAssemble: Showing assemble screen");
            Collection<GameObject> gameObjects = _parent.getGameObjects();
            for (GameObject go : gameObjects){
                go.render(renderer, ms);
            }
        }
    }
}