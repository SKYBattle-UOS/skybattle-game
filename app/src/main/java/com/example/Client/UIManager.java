package com.example.Client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public class UIManager {
    private Screen _currentScreen = null;
    private Map<Integer, Runnable> _callbackMapping = new HashMap<>();
    private Queue<ScreenType> _toSwitch = new LinkedList<>();

    public void switchScreen(ScreenType type){
        _toSwitch.add(type);

        if (_currentScreen != null)
            _currentScreen.switchTo(_toSwitch.remove());
    }

    public void setCurrentScreen(Screen screen){
        _currentScreen = screen;

        if (!_toSwitch.isEmpty()){
            _currentScreen.switchTo(_toSwitch.remove());
        }
    }

    public void invoke(int port){
        Runnable func = _callbackMapping.get(port);
        if (func != null)
            func.run();
    }

    public void registerCallback(int port, Runnable func){
        Objects.requireNonNull(func);
        _callbackMapping.put(port, func);
    }
}
