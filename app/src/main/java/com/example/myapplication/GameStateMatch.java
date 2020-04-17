package com.example.myapplication;

enum MatchStateType {
    ASSEMBLE,
    SELECT_CHARACTER,
    GET_READY,
    INGAME,
    END
}

public class GameStateMatch implements GameState {
    /* ===== DEBUG START ===== */

    private int __count = 0;
    private TempPlayer _thisPlayer;
    private TempPlayer _anotherPlayer;
    private TempPlayer _yetAnotherPlayer;

    /* ===== DEBUG END ===== */

    final int GETREADYCOUNT = 10000; // 10 seconds
    private GameState _currentState;

    GameStateMatch(){
        _currentState = new MatchStateAssemble();
    }

    @Override
    public void run(int ms) {
        _currentState.run(ms);

        /* ===== DEBUG START ===== */

        __count++;
        if (__count == 4)
            // assembled
            switchState(MatchStateType.SELECT_CHARACTER);
        if (__count == 7)
            // character selected
            createCharacters();
            switchState(MatchStateType.GET_READY);

        /* ===== DEBUG END ===== */
    }

    public void switchState(MatchStateType matchState){
        switch (matchState){
            case ASSEMBLE:
                _currentState = new MatchStateAssemble();
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

    public void createPlayers(){}

    public void createCharacters(){
        _thisPlayer = new TempPlayer("ThisPlayer");
        _anotherPlayer = new TempPlayer("AnotherPlayer");
        _yetAnotherPlayer = new TempPlayer("YetAnotherPlayer");
    }

    public GameObject[] getGameObjects(){
        return new GameObject[] { _thisPlayer, _anotherPlayer, _yetAnotherPlayer };
    }
}
