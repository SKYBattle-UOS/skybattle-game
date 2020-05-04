package com.example.myapplication;

/**
 * GameStateContext 에서 사용하는 Enum.
 * @see GameStateContext
 * @see MatchStateType
 */
enum GameStateType {
    /**
     * 메인 화면
     * @see GameStateMain
     */
    MAIN,
    /**
     * 방 화면
     * @see GameStateRoom
     */
    ROOM,
    /**
     * 매치 화면
     * @see GameStateMatch
     */
    MATCH
}

/**
 * 애플리케이션의 매치와 관련 없는 각 화면에 대한 상태패턴의 Context 클래스.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 * @see GameStateMatch
 */
public class GameStateContext {
    private GameState _currentState;

    /**
     * 현재 상태를 호출하는 클래스.
     * @param ms 지난 프레임부터 경과한 밀리세컨드.
     */
    public void update(long ms){
        _currentState.update(ms);
    }

    public void render(Renderer renderer, long ms){
        _currentState.render(renderer, ms);
    }

    /**
     * 현재 상태를 변경하는 함수.
     * @param gameState 현재 상태를 이 상태로 변경함.
     * @see GameStateType
     */
    public void switchState(GameStateType gameState){
        switch (gameState){
            case MAIN:
                _currentState = new GameStateMain(this);
                break;
            case ROOM:
                _currentState = new GameStateRoom(this);
                break;
            case MATCH:
                _currentState = new GameStateMatch();
                break;
        }
        _currentState.start();
    }
}
