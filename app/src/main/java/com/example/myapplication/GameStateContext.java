package com.example.myapplication;

enum GameStateType {
    MAIN,
    MATCH
}

public class GameStateContext {
    private GameState _currentState;

    GameStateContext(){
        _currentState = new GameStateMain();
    }

    public void run(int ms){
        _currentState.run(ms);
    }

    public void switchState(GameStateType gameState){
        switch (gameState){
            case MAIN:
                _currentState = new GameStateMain();
                break;
            case MATCH:
                _currentState = new GameStateMatch();
                break;
        }
    }
}
