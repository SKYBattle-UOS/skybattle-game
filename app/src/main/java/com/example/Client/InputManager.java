package com.example.Client;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import Common.InputState;
import Common.LatLonByteConverter;
import Common.OutputBitStream;

public class InputManager {
//    private Location _location;
//    private double[] _prevLatlon;
    private Queue<InputState> _inputStates;
    private LatLonByteConverter _converter;

    // TODO
    private long _elapsed;
    private double lat = 37.714775;
    private double lon = 127.043325;
    private double destLat = 37.715584;
    private double destLon = 127.048616;
    private int step = 0;
    private int[] _convertTemp = new int[2];

    public InputManager(Context context, LatLonByteConverter converter){
//        _location = new Location(context);
//        _prevLatlon = new double[2];
        _inputStates = new LinkedList<>();
        _converter = converter;
    }

    public void update(long ms){
        sendInput();
        updateInputState();

        // TODO
        _elapsed += ms;
    }

    private void updateInputState() {
//        double[] latlon = _location.getCurrentLocation();
//        if (isPosDirty(latlon)){
//            // TODO: if qwer dirty
//            InputState newState = new InputState();
//            newState.lat = latlon[0];
//            newState.lon = latlon[1];
//            _prevLatlon = latlon;
//
//            _inputStates.offer(newState);
//        }
        if (_elapsed > 500 && step <= 100){
            InputState newState = new InputState();
            _converter.convertLatLon(lat + (destLat - lat) / 100 * step, lon + (destLon - lon) / 100 * step, _convertTemp);
            newState.lat = _convertTemp[0];
            newState.lon = _convertTemp[1];
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

//    private boolean isPosDirty(double[] latlon) {
//        // TODO: only dirty if position changed by a significant amount
//        return (latlon[0] * latlon[1]) != 0 && (latlon[0] != _prevLatlon[0] && latlon[1] != _prevLatlon[1]);
//    }
}
