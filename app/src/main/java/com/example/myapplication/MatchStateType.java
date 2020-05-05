package com.example.myapplication;

/**
 * GameStateMatch에서 상태 전환을 위해 사용하는 Enum
 * @see GameStateMatch
 * @see GameStateType
 */
public enum MatchStateType {
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