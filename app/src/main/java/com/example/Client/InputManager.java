package com.example.Client;

import android.content.Context;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.InputState;
import Common.LatLonByteConverter;
import Common.OutputBitStream;
import Common.Player;
import Host.SkillTarget;

public class InputManager {
//    private Location _location;
//    private double[] _prevLatlon;
    private Queue<InputState> _inputStates;
    private LatLonByteConverter _converter;
    private boolean _dirty;

    // TODO
    private long _elapsed;
    private double lat = 37.714775;
    private double lon = 127.043325;
    private double destLat = 37.715584;
    private double destLon = 127.048616;
    private int step = 0;
    private int[] _convertTemp = new int[2];
    private PlayerClient _player;
    private boolean _startSending;
    private Location location;

    public InputManager(Context context, LatLonByteConverter converter){
        _inputStates = new ConcurrentLinkedQueue<>();
        _converter = converter;
        location = new Location(context);
    }

    public void update(long ms){
        if (!_startSending)
            return;

        sendInput();

        boolean isTest = false; //true - 실제 디바이스 false - 테스트 -> 가상 시뮬로 테스트 사용
        if (isTest){
            double[] _newPos = location.getLocation();

            InputState state = new InputState();
            state.command = 0;
            _converter.convertLatLon(_newPos[0], _newPos[1], _convertTemp);
            state.lat = _convertTemp[0];
            state.lon = _convertTemp[1];
            _inputStates.offer(state);
        }
//        else
//            debugMoveToAssemblePoint();


        // TODO
        _elapsed += ms;
    }

    public void debugMove(int direction) {
        Player player = Core.get().getMatch().getThisPlayer();

        if (player == null) return;

        double[] _newPos = player.getGameObject().getPosition();
        switch (direction){
            case 0:
                _newPos[0] += 0.00005;
                break;
            case 1:
                _newPos[0] -= 0.00005;
                break;
            case 2:
                _newPos[1] += 0.00005;
                break;
            case 3:
                _newPos[1] -= 0.00005;
                break;
        }

        InputState state = new InputState();
        state.command = 0;
        _converter.convertLatLon(_newPos[0], _newPos[1], _convertTemp);
        state.lat = _convertTemp[0];
        state.lon = _convertTemp[1];
        _inputStates.offer(state);
    }

    private void debugMoveToAssemblePoint(){
        if (_elapsed > 100 && step <= 100){
            InputState newState = new InputState();
            _converter.convertLatLon(lat + (destLat - lat) / 100 * step, lon + (destLon - lon) / 100 * step, _convertTemp);
            newState.lat = _convertTemp[0];
            newState.lon = _convertTemp[1];
            newState.command = 0;
            _inputStates.offer(newState);
            _elapsed = 0;
            step++;
        }
    }

    private void sendInput() {
        OutputBitStream stream = Core.get().getPakcetManager().getPacketToSend();

        InputState input = _inputStates.poll();
        if (input != null){
            // sending one input in the packet
            stream.write(1, 2);

            input.writeToStream(stream);
            Core.get().getPakcetManager().shouldSendThisFrame();
        }
        else {
            // sending 0 input in the packet
            stream.write(0, 2);
        }
    }

    public void castSkill(int qwer, SkillTarget target){
        InputState state = new InputState();
        state.command = qwer + 1;
        state.playerId = target.networkId;
        if (target.lat * target.lon != 0){
            _converter.convertLatLon(target.lat, target.lon, _convertTemp);
            state.lat = _convertTemp[0];
            state.lon = _convertTemp[1];
        }
        _inputStates.offer(state);
    }

    public void startSending(){
        _startSending = true;
    }
}
