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
    private static InputManager inputManager = new InputManager();
    private static Renderer renderer = new Renderer();
    private static GameStateContext stateContext = new GameStateContext();
    private static InstructionIOManager ioManager = new InstructionFileManager();
    private static WorldSetter worldSetter = new WorldSetter();
    private static byte[] packetBuffer = new byte[1300];
    private static TempOutputBitStream packetOutStream = new TempOutputBitStream(packetBuffer);
    private static TempInputBitStream packetInStream = new TempInputBitStream(packetBuffer);

    /**
     * 애플리케이션 로직의 시작점.
     */
    public void run(){

        // TODO: DEBUG DELETE
        // region DEBUG
        int ms = 1000; // update every one second (1000 millisecond)

        run(ms);
        run(ms);

        // match start button pressed
        stateContext.switchState(GameStateType.MATCH);

        // match runs
        for (int i = 0; i < 30; i++){
            run(ms);
        }
        // endregion
    }

    // TODO: DEBUG DELETE
    // region DEBUG
    public void run(int ms){
        packetInStream.reset();
        packetOutStream.reset();

        ioManager.receive(packetOutStream);
        worldSetter.processInstructions(packetInStream);

        stateContext.update(ms);
        renderer.render(ms);

        inputManager.update(ms);
        ioManager.send();
    }
    // endregion
}
