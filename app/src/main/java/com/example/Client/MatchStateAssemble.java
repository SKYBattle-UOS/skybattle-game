package com.example.Client;

import java.util.Collection;

import Common.GameObject;
import Common.GameState;
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
    private int _numPlayers;
    private IOManager _ioManager;

    public MatchStateAssemble(GameStateMatch parentMatch, int numPlayers) {
        _parent = parentMatch;
        _isInitialized = false;
        _sentConfirm = false;
        _numPlayers = numPlayers;
        _ioManager = Core.getInstance().getIOManager();
    }

    @Override
    public void update(long ms) {
        if (!_sentConfirm){
            Collection<GameObject> gos = _parent.getGameObjects();
            if (gos.size() >= _numPlayers){
                _ioManager.confirmPlayerInit();
                _sentConfirm = true;
            }
        }

        _isInitialized = _ioManager.isEverybodyInitializedForAssemble();

        if (_ioManager.isAssembleComplete()){
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
}