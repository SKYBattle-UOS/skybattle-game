package com.example.Client;

import android.content.Context;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Common.InputState;
import Common.LatLonByteConverter;
import Common.OutputBitStream;
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
    private Player _player;
    private boolean _startSending;

    public InputManager(Context context, LatLonByteConverter converter){
        _inputStates = new ConcurrentLinkedQueue<>();
        _converter = converter;
    }

    public void update(long ms){
        if (!_startSending) return;
        debugMoveToAssemblePoint();
        sendInput();

        // TODO
        _elapsed += ms;
    }

    public void setThisPlayer(Player player){
        _player = player;
    }

    public void debugMove(int direction) {
        double[] _newPos = _player.getPosition();
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
        state.qwer = 4;
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
            newState.qwer = 4;
            _inputStates.offer(newState);
            _elapsed = 0;
            step++;
        }
    }

    private void sendInput() {
        OutputBitStream stream = Core.getInstance().getPakcetManager().getPacketToSend();

        InputState input = _inputStates.poll();
        if (input != null){
            // sending one input in the packet
            try {
                stream.write(1, 2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            input.writeToStream(stream);
            Core.getInstance().getPakcetManager().shouldSendThisFrame();
        }
        else {
            // sending 0 input in the packet
            try {
                stream.write(0, 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void qwer(SkillTarget target){
        InputState state = new InputState();
        state.qwer = target.qwer;
        state.playerId = target.playerId;
        if (target.lat * target.lon != 0){
            _converter.convertLatLon(target.lat, target.lon, _convertTemp);
            state.lat = _convertTemp[0];
            state.lon = _convertTemp[1];
        }
        _inputStates.offer(state);
    }

    public Player getThisPlayer(){
        return _player;
    }

    public void startSending(){
        _startSending = true;
    }
}
