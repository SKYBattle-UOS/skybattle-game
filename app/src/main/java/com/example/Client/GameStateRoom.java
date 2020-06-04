package com.example.Client;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;
import Common.RoomSettings;

public class GameStateRoom implements GameState {
    private GameStateContext _parent;
    private boolean _waiting = false;
    private RoomSettings _settings = new RoomSettings();
    private RoomSettings _settingsToSend = new RoomSettings();

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;

        receive();

        if (Core.get().isHost())
            sendHost();
    }

    private void receive() {
        InputBitStream packet = Core.get().getPakcetManager().getPacketStream();
        if (packet == null) return;

        _settings.readFromStream(packet);

        if (_settings.roomTitleChanged)
            onRoomTitleChanged();

        if (_settings.startButtonPressed)
            onGameStarted();

        _settings = new RoomSettings();
    }

    private void sendHost() {
        OutputBitStream outPacket = Core.get().getPakcetManager().getPacketToSend();
        if ((_settingsToSend.writeToStream(outPacket)))
            Core.get().getPakcetManager().shouldSendThisFrame();

        _settingsToSend = new RoomSettings();
    }

    private void onRoomTitleChanged() {
        Core.get().getUIManager().setTitle(_settings.roomTitle);
        _settings.roomTitleChanged = false;
    }

    private void onGameStarted() {
        _waiting = true;
        Core.get().getInputManager().startSending();
        Core.get().getUIManager().switchScreen(ScreenType.MAP,
                ()->_parent.switchState(GameStateType.MATCH));
    }

    public void startGame(){
        _settingsToSend.startButtonPressed = true;
    }

    public void changeRoomTitle(String newTitle){
        _settingsToSend.roomTitleChanged = true;
        _settingsToSend.roomTitle = newTitle;
    }

    public void exitRoom() {
        Core.get().getUIManager().switchScreen(ScreenType.MAIN,
                ()->_parent.switchState(GameStateType.MAIN));
    }
}