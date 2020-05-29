package com.example.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import Common.Collider;
import Common.GameObject;
import Common.GameState;
import Common.InputBitStream;
import Common.LatLonByteConverter;
import Common.MatchCommon;
import Common.MatchStateType;
import Common.PlayerCommon;
import Common.TimerStruct;
import Host.WorldSetterHost;

/**
 * 앱의 각 화면에 대한 상태패턴의 상태 객체 중 매치화면.
 * 동시에 매치의 각 화면에 대한 상태패턴의 Context 클래스.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 * @see GameStateContext
 */
public class GameStateMatch implements GameState, Match {
    private GameStateContext _parent;
    private WorldSetter _worldSetter;
    private GameObjectRegistry _gameObjectRegistry;
    private Vector<GameObject> _gameObjects;
    private ArrayList<PlayerCommon> _players;
    private int _numPlayers;
    private boolean _worldSetterActive = false;
    private double[] _battleGroundLatLon;
    private PriorityQueue<TimerStruct> _timerQueue = new PriorityQueue<>();

    private GameState _currentState;

    GameStateMatch(GameStateContext parent){
        // TODO: for now, only 1 player
        _numPlayers = 1;

        _parent = parent;
        _currentState = new MatchStateAssemble(this, _numPlayers);
        _gameObjectRegistry = new GameObjectRegistry();
        _gameObjects = new Vector<>();
        _players = new ArrayList<>();
        _worldSetter = new WorldSetter(this);
        _battleGroundLatLon = new double[2];
    }

    @Override
    public void start() {
        Core.get().setMatch(this);
    }

    @Override
    public void update(long ms) {
        InputBitStream packetStream = Core.get().getPakcetManager().getPacketStream();
        if (packetStream != null && _worldSetterActive)
            _worldSetter.processInstructions(packetStream);

        processTimers();
        killGameObjects();

        for (GameObject go : _gameObjects)
            go.before(ms);

        for (GameObject go : _gameObjects)
            go.update(ms);

        for (GameObject go : _gameObjects)
            go.after(ms);

        _currentState.update(ms);
    }

    private void processTimers() {
        while (true){
            TimerStruct ts = _timerQueue.peek();
            if (ts == null) return;

            if (ts.timeToBeFired < Core.get().getTime().getStartOfFrame()){
                ts.callback.run();
                _timerQueue.poll();
            }
            else
                return;
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

    @Override
    public LatLonByteConverter getConverter() {
        return _parent.getConverter();
    }

    @Override
    public Collider getCollider() {
        return null;
    }

    @Override
    public GameObjectRegistry getRegistry() { return _gameObjectRegistry; }

    @Override
    public List<GameObject> getWorld() { return _gameObjects; }

    @Override
    public List<PlayerCommon> getPlayers() {
        return _players;
    }

    @Override
    public void setTimer(Runnable callback, float seconds) {
        long timeToBeFired = Core.get().getTime().getStartOfFrame();
        timeToBeFired += (long) seconds * 1000;
        _timerQueue.add(new TimerStruct(callback, timeToBeFired));
    }

    @Override
    public Player getThisPlayer() {
        // TODO
        Player player;
        List<PlayerCommon> gos = getPlayers();
        for (PlayerCommon go : gos){
            if (go.getPlayerId() == 0) {
                player = (Player) go;
                return player;
            }
        }
        return null;
    }

    private void killGameObjects(){
        int goSize = _gameObjects.size();
        for (int i = 0; i < goSize; i++) {
            GameObject gameObject = _gameObjects.get(i);
            if (gameObject.wantsToDie()) {
                gameObject.faceDeath();
                _gameObjects.set(gameObject.getIndexInWorld(), _gameObjects.get(_gameObjects.size() - 1));
                _gameObjects.get(gameObject.getIndexInWorld()).setIndexInWorld(gameObject.getIndexInWorld());
                _gameObjects.remove(_gameObjects.size() - 1);
                goSize--;
                i--;
            }
        }
    }

    public void activateWorldSetter(){
        _worldSetterActive = true;
    }

    public double[] getBattleGroundLatLon() {
        return _battleGroundLatLon;
    }

    public void setBattleGroundLatLon(double lat, double lon) {
        _battleGroundLatLon[0] = lat;
        _battleGroundLatLon[1] = lon;
        _parent.getConverter().setOffset(lat, lon);
    }
}
