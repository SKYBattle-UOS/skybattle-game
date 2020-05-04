package com.example.myapplication;

/**
 * 앱의 각 화면에 대한 상태패턴의 상태 객체의 인터페이스
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public interface GameState {
    default void start(){}
    /**
     * 상태 객체를 실행하는 함수.
     * @param ms 지난 프레임부터 경과한 밀리세컨드.
     */
    void update(long ms);
    void render(Renderer renderer, long ms);
}