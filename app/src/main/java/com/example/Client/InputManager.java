package com.example.Client;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import Common.GameObject;
import Common.InputState;
import Common.LatLonByteConverter;
import Common.OutputBitStream;

public class InputManager {
//    private Location _location;
//    private double[] _prevLatlon;
    private Queue<InputState> _inputStates;
    private LatLonByteConverter _converter;
    private boolean _q;
    private boolean _w;
    private boolean _e;
    private boolean _r;
    private boolean _dirty;

    // TODO
    private long _elapsed;
    private double lat = 37.714775;
    private double lon = 127.043325;
    private double destLat = 37.715584;
    private double destLon = 127.048616;
    private int step = 0;
    private int[] _convertTemp = new int[2];
    private GameObject _player;
    private int _direction;
    private double[] _newPos;

    public InputManager(Context context, LatLonByteConverter converter){
        _inputStates = new LinkedList<>();
        _converter = converter;
    }

    public void update(long ms){
        sendInput();
        updateInputState();

        // TODO
        _elapsed += ms;
    }

    private synchronized void updateInputState() {
        debugMoveToAssemblePoint();

        if (_dirty){
            addInput();
            _dirty = false;
        }
    }

    private void addInput() {
        InputState newState = new InputState();
        _converter.convertLatLon(_newPos[0], _newPos[1], _convertTemp);
        newState.lat = _convertTemp[0];
        newState.lon = _convertTemp[1];
        newState.q = _q;
        newState.w = _w;
        newState.e = _e;
        newState.r = _r;
        _inputStates.offer(newState);

        _q = false;
        _w = false;
        _e = false;
        _r = false;
    }

    public void setDebugPlayer(GameObject player){
        _player = player;
    }

    public void debugMove(int direction) {
        _newPos = _player.getPosition();
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
        _dirty = true;
    }

    private void debugMoveToAssemblePoint(){
        if (_elapsed > 125 && step <= 100){
            InputState newState = new InputState();
            _converter.convertLatLon(lat + (destLat - lat) / 100 * step, lon + (destLon - lon) / 100 * step, _convertTemp);
            newState.lat = _convertTemp[0];
            newState.lon = _convertTemp[1];
            newState.q = _q;
            newState.w = _w;
            newState.e = _e;
            newState.r = _r;
            _inputStates.offer(newState);
            _elapsed = 0;
            step++;

            _q = false;
            _w = false;
            _e = false;
            _r = false;
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

    public synchronized void q(){
        _q = true;
        _dirty = true;
    }

    public synchronized void w(){
        _w = true;
        _dirty = true;
    }

    public synchronized void e(){
        _e = true;
        _dirty = true;
    }

    public synchronized void r(){
        _r = true;
        _dirty = true;
    }
}
