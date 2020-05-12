package com.example.Client;

import android.content.Context;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import Common.InputState;
import Common.OutputBitStream;

public class InputManager {
    private Location _location;
    private Queue<InputState> _inputStates;
    private double[] _prevLatlon;

    public InputManager(Context context){
        _location = new Location(context);
        _inputStates = new LinkedList<>();
        _prevLatlon = new double[2];
    }

    public void update(long ms){
        sendInput();
        updateInputState();
    }

    private void updateInputState() {
        double[] latlon = _location.getCurrentLocation();
        if (isPosDirty(latlon)){
            // TODO: if qwer dirty
            InputState newState = new InputState();
            newState.lat = latlon[0];
            newState.lon = latlon[1];
            _prevLatlon = latlon;

            _inputStates.offer(newState);
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
        }

        // sending 0 input in the packet
        try {
            stream.write(0, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPosDirty(double[] latlon) {
        // TODO: only dirty if position changed by a significant amount
        return (latlon[0] * latlon[1]) != 0 && (latlon[0] != _prevLatlon[0] && latlon[1] != _prevLatlon[1]);
    }
}
