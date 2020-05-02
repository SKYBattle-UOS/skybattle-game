package com.example.myapplication;

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
    // TODO: DEBUG DELETE
    // region DEBUG
    private int __count = 0;
    private TempPlayer _thisPlayer;
    private TempPlayer _anotherPlayer;
    private TempPlayer _yetAnotherPlayer;
    // endregion

    // TODO: DEBUG EDIT
    final int GETREADYCOUNT = 10000; // 10 seconds
    private GameState _currentState;

    GameStateMatch(){
        _currentState = new MatchStateAssemble(this);
    }

    @Override
    public void update(int ms) {
        _currentState.update(ms);
    }

    /**
     * 현재 상태를 변경하는 함수.
     * @param matchState 현재 상태를 이 상태로 변경함.
     */
    public void switchState(MatchStateType matchState){
        switch (matchState){
            case ASSEMBLE:
                _currentState = new MatchStateAssemble(this);
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
     * 방에서 게임시작을 누르면 매치를 시작한 플레이어들을 생성하기 위한 함수.
     */
    public void createPlayers(){
        // TODO: DEBUG EDIT
    }

    /**
     * 캐릭터 선택이 완료되면 캐릭터를 생성하기 위한 함수.
     */
    public void createCharacters(){
        // TODO: DEBUG EDIT
        _thisPlayer = new TempPlayer("ThisPlayer");
        _anotherPlayer = new TempPlayer("AnotherPlayer");
        _yetAnotherPlayer = new TempPlayer("YetAnotherPlayer");
    }

    /**
     * @return 현재 매치에 존재한는 모든 GameObject의 Array
     */
    public GameObject[] getGameObjects(){
        // TODO: DEBUG EDIT
        return new GameObject[] { _thisPlayer, _anotherPlayer, _yetAnotherPlayer };
    }
}
