package com.example.Client;

import java.util.ArrayList;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;
import Common.OutputBitStream;
import Common.RoomSettings;
import Common.RoomUserInfo;
import Common.Util;

public class GameStateRoom implements GameState {
    private GameStateContext _parent;
    private boolean _waiting = false;
    private RoomSettings _settings = new RoomSettings();
    private RoomSettings _settingsToSend = new RoomSettings();
    private boolean _infoDirty;

    private ArrayList<RoomUserInfo> _roomUserInfos = new ArrayList<>();
    private RoomUserInfo _myRoomUserInfo;

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;

        receive();

        if (Core.get().isHost())
            sendHost();

        send();
    }

    private void send() {
        OutputBitStream packet = Core.get().getPakcetManager().getPacketToSend();
        Util.sendHas(packet, _infoDirty);
        if (_infoDirty){
            Core.get().getPakcetManager().shouldSendThisFrame();
            _myRoomUserInfo.writeToStream(packet);
            _infoDirty = false;
        }
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

        if (Util.hasMessage(packet)){
            _roomUserInfos.clear();
            int numUsers = packet.read(8);
            for (int i = 0; i < numUsers; i++){
                RoomUserInfo info = new RoomUserInfo();
                info.readFromStream(packet);
                if (info.playerId == Core.get().getPakcetManager().getPlayerId())
                    _myRoomUserInfo = info;

                _roomUserInfos.add(info);
            }
            Core.get().getUIManager().setRoomUserInfos(_roomUserInfos);
        }
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
        Core.get().getUIManager().switchScreen(ScreenType.MAIN, null);
    }

    public void setUserName(String name){
        _myRoomUserInfo.name = name;
        _infoDirty = true;
    }

    public void setTeam(int team){
        _myRoomUserInfo.team = team;
        _infoDirty = true;
    }
}