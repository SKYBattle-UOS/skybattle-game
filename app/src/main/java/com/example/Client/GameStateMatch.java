package com.example.Client;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Vector;

import Common.CharacterFactory;
import Common.Collider;
import Common.GameObject;
import Common.GameState;
import Common.InputBitStream;
import Common.LatLonByteConverter;
import Common.MatchStateType;
import Common.Player;
import Common.ReadOnlyList;
import Common.TimerStruct;
import Common.Util;

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
    private ArrayList<Player> _players;
    private ReadOnlyList<GameObject> _readOnlyGameObjects;
    private ReadOnlyList<Player> _readOnlyPlayers;
    private boolean _worldSetterActive = false;
    private double[] _battleGroundLatLon;
    private PriorityQueue<TimerStruct> _timerQueue = new PriorityQueue<>();

    private GameState _currentState;
    private CharacterFactory _charFactory;

    GameStateMatch(GameStateContext parent){
        _parent = parent;
        _currentState = new MatchStateAssemble(this);
        _gameObjectRegistry = new GameObjectRegistry();
        _gameObjects = new Vector<>();
        _players = new ArrayList<>();
        _worldSetter = new WorldSetter(this, _gameObjects, _players);
        _battleGroundLatLon = new double[2];
        _charFactory = new CharacterFactory(Core.get().getGameObjectFactory());

        _readOnlyGameObjects = new ReadOnlyList<>(_gameObjects);
        _readOnlyPlayers = new ReadOnlyList<>(_players);

    }

    @Override
    public void start() {
        Core.get().setMatch(this);
        Util.registerGameObjects(Core.get().getGameObjectFactory(), this, Core.get().getUIManager());
    }

    @Override
    public void update(long ms) {
        processTimers();
        killGameObjects();

        for (GameObject go : _gameObjects)
            go.before(ms);

        InputBitStream packetStream = Core.get().getPakcetManager().getPacketStream();
        if (packetStream != null && _worldSetterActive)
            _worldSetter.processInstructions(packetStream);

        for (GameObject go : _gameObjects)
            go.update(ms);

        _currentState.update(ms);

        for (GameObject go : _gameObjects)
            go.after(ms);
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
                _currentState = new MatchStateAssemble(this);
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
            case GAMEOVER:
                _currentState = new MatchStateGameOver(this);
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
    public ReadOnlyList<GameObject> getWorld() { return _readOnlyGameObjects; }

    @Override
    public ReadOnlyList<Player> getPlayers() {
        return _readOnlyPlayers;
    }

    @Override
    public CharacterFactory getCharacterFactory() {
        return _charFactory;
    }

    @Override
    public void setTimer(Object timerOwner, Runnable callback, float seconds) {
        long timeToBeFired = Core.get().getTime().getStartOfFrame();
        timeToBeFired += (long) seconds * 1000;
        _timerQueue.add(new TimerStruct(timerOwner, callback, timeToBeFired));
    }

    @Override
    public void killAllTimers(Object owner) {
        ArrayList<TimerStruct> toKill = new ArrayList<>();
        for (TimerStruct ts : _timerQueue){
            if (ts.owner == owner){
                toKill.add(ts);
            }
        }

        for (TimerStruct ts : toKill){
            _timerQueue.remove(ts);
        }
    }

    @Override
    public Player getThisPlayer() {
        return Util.findPlayerById(this, Core.get().getPakcetManager().getPlayerId());
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

    public void setPlayerId(int playerId){

    }

    public GameState getState() {
        return _currentState;
    }
}
