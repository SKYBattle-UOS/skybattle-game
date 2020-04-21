package com.example.myapplication;

/**
 * 앱이 사용하는 여러 클래스를 초기화하고 작동순서대로 호출합니다.
 * 게임루프가 들어있습니다.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class Core {
    private static Renderer renderer = new Renderer();
    private static GameStateContext stateContext = new GameStateContext();

    /**
     * 애플리케이션 로직의 시작점.
     */
    public void run(){

        // TODO: DEBUG DELETE
        /* ===== DEBUG START ===== */
        int ms = 1000; // update every one second (1000 millisecond)

        stateContext.run(ms);
        stateContext.run(ms);

        // match start button pressed
        stateContext.switchState(GameStateType.MATCH);

        // match runs
        for (int i = 0; i < 30; i++)
            stateContext.run(ms);
        /* ===== DEBUG END ===== */
    }
}
