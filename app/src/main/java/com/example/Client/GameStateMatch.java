package com.example.Client;

import java.util.Collection;
import java.util.Vector;

import Common.GameObject;
import Common.GameState;
import Common.InputBitStream;
import Common.MatchStateType;

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
    private Vector<GameObject> _gameObjects;
    private int _numPlayers;

    private GameState _currentState;

    GameStateMatch(){
        // TODO: for now, only 1 player
        _numPlayers = 1;
        _currentState = new MatchStateAssemble(this, _numPlayers);
        _gameObjectRegistry = new GameObjectRegistry();
        _gameObjects = new Vector<>();
        _worldSetter = new WorldSetter(_gameObjects, _gameObjectRegistry);
    }

    @Override
    public void update(long ms) {
        InputBitStream packetStream = Core.getInstance().getPakcetManager().getPacketStream();
        if (packetStream != null)
            _worldSetter.processInstructions(packetStream);

        for (GameObject go : _gameObjects){
            if (!go.doesWantToDie())
                go.update(ms);
        }

        _currentState.update(ms);

        int goSize = _gameObjects.size();
        for (int i = 0; i < goSize; i++)
            if (_gameObjects.get(i).doesWantToDie()){
                killGameObject(_gameObjects.get(i));
                goSize--;
                i--;
            }
    }

    @Override
    public void render(Renderer renderer, long ms) {
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
                _currentState = new MatchStateSelectCharacter(this);
                break;
            case GET_READY:
                _currentState = new MatchStateGetReady(this);
                break;
            case INGAME:
                _currentState = new MatchStateInGame(this);
                break;
        }
        _currentState.start();
    }

    /**
     * @return 현재 매치에 존재한는 모든 GameObject의 Array
     */
    public Collection<GameObject> getGameObjects(){
        // TODO: DEBUG EDIT
        return _gameObjects;
    }

    public WorldSetter getWorldSetter(){
        return _worldSetter;
    }

    private void killGameObject(GameObject gameObject){
        gameObject.faceDeath();
        _gameObjects.set(gameObject.getIndexInWorld(), _gameObjects.get(_gameObjects.size() - 1));
        _gameObjects.remove(_gameObjects.size() - 1);
    }
}
