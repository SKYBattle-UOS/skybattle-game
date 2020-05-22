package com.example.Client;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public class UIManager {
    public static final int ENTER_ROOM_PORT = 0;
    public static final int ROOM_START_PORT = 0;
    public static final int EXIT_ROOM_PORT = 1;

    private Screen _currentScreen = null;
    private ScreenType _nextScreen = null;
    private Runnable _onComplete = null;
    private boolean _shouldSendSwitch = false;
    private final Object _mutex = new Object();

    private Map<Integer, Runnable> _callbackMapping = new HashMap<>();
    private Handler _mainHandler = new Handler(Looper.getMainLooper());

    public void switchScreen(ScreenType type, Runnable onComplete){
        _nextScreen = type;
        _onComplete = onComplete;
        _shouldSendSwitch = true;

        synchronized (_mutex) {
            if (_currentScreen != null) {
                _mainHandler.post(()->_currentScreen.switchTo(type));
                _shouldSendSwitch = false;
            }
        }
    }

    public void setCurrentScreen(Screen screen, ScreenType type){
        synchronized (_mutex){
            _currentScreen = screen;
            if (_currentScreen == null) return;

            if (_shouldSendSwitch) {
                _mainHandler.post(()->_currentScreen.switchTo(_nextScreen));
                _shouldSendSwitch = false;
            }
            else if (type == _nextScreen){
                _onComplete.run();
                _nextScreen = null;
                _onComplete = null;
            }
        }
    }

    public void invoke(int port){
        Runnable func = _callbackMapping.get(port);
        if (func != null) {
            func.run();
        }
    }

    public void registerCallback(int port, Runnable func){
        Objects.requireNonNull(func);
        _callbackMapping.put(port, func);
    }

    public void setTitle(String title){
        if (_currentScreen instanceof RoomScreen)
            _mainHandler.post(()->((RoomScreen) _currentScreen).setTitle(title));
    }

    public void setTopText(String text){
        if (_currentScreen instanceof MatchScreen)
            _mainHandler.post(()->((MatchScreen) _currentScreen).setTopText(text));
    }
}
