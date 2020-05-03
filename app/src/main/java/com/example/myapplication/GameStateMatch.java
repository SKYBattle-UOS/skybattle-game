package com.example.myapplication;

import android.util.Log;

import java.util.Collection;

/**
 * GameStateMatch에서 상태 전환을 위해 사용하는 Enum
 * @see GameStateMatch
 * @see GameStateType
 */
enum MatchStateType {
    /**
     * 집합화면
     * @see MatchStateAssemble
     */
    ASSEMBLE,
    /**
     * 캐릭터 선택 화면
     * @see MatchStateSelectCharacter
     */
    SELECT_CHARACTER,
    /**
     * 준비하는 화면. 러너들이 도망가기 위한 시간.
     * @see MatchStateGetReady
     */
    GET_READY,
    /**
     * 게임 진행 중 화면.
     * @see MatchStateInGame
     */
    INGAME,
    /**
     * 게임 종료 후 재집합 안내 화면.
     */
    END
}

/**
 * 앱의 각 화면에 대한 상태패턴의 상태 객체 중 매치화면.
 * 동시에 매치의 각 화면에 대한 상태패턴의 Context 클래스.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 * @see GameStateContext
 */
public class GameStateMatch implements GameState {
    private WorldSetter _worldSetter;
    private GameObjectRegistry _gameObjectRegistry;
    private int _numPlayers;

    // TODO: DEBUG EDIT
    final int GETREADYCOUNT = 10000; // 10 seconds
    private GameState _currentState;

    GameStateMatch(){
        // TODO: for now, only 2 players
        _numPlayers = 2;
        _currentState = new MatchStateAssemble(this, _numPlayers);
        _gameObjectRegistry = new GameObjectRegistry();
        _worldSetter = new WorldSetter(_gameObjectRegistry);
    }

    @Override
    public void update(int ms) {
        InputBitStream packetStream = Core.getInstance().getInstructionManager().getPacketStream();
        if (packetStream != null)
            _worldSetter.processInstructions(packetStream);

        Collection<GameObject> gameObjects = getGameObjects();
        for (GameObject go : gameObjects){
            if (!go.doesWantToDie())
                go.update(ms);
        }

        _currentState.update(ms);

        for (GameObject go : gameObjects){
            if (go.doesWantToDie()){
                go.faceDeath();
                _gameObjectRegistry.remove(go);
            }
        }
    }

    @Override
    public void render(Renderer renderer, int ms) {
        _currentState.render(renderer, ms);
    }

    /**
     * 현재 상태를 변경하는 함수.
     * @param matchState 현재 상태를 이 상태로 변경함.
     */
    public void switchState(MatchStateType matchState) {
        switch (matchState) {
            case ASSEMBLE:
                _currentState = new MatchStateAssemble(this, _numPlayers);
                break;
            case SELECT_CHARACTER:
                _currentState = new MatchStateSelectCharacter();
                break;
            case GET_READY:
                _currentState = new MatchStateGetReady(this, GETREADYCOUNT);
                break;
            case INGAME:
                _currentState = new MatchStateInGame(this);
                break;
        }
    }

    /**
     * @return 현재 매치에 존재한는 모든 GameObject의 Array
     */
    public Collection<GameObject> getGameObjects(){
        // TODO: DEBUG EDIT
        return _gameObjectRegistry.getGameObjects();
    }

    public WorldSetter getWorldSetter(){
        return _worldSetter;
    }
}
