package com.example.Client;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public class AndroidUIManager implements UIManager {
    private Screen _currentScreen = null;
    private ScreenType _nextScreen = null;
    private Runnable _onComplete = null;
    private boolean _shouldSendSwitch = false;
    private final Object _mutex = new Object();

    private Map<Integer, Runnable> _callbackMapping = new HashMap<>();
    private Handler _mainHandler = new Handler(Looper.getMainLooper());

    @SuppressWarnings("unchecked")
    private MutableLiveData<String>[] _qwerTexts = new MutableLiveData[4];
    @SuppressWarnings("unchecked")
    private MutableLiveData<Boolean>[] _qwerEnables = new MutableLiveData[4];
    private MutableLiveData<String> _topText = new MutableLiveData<>();
    private MutableLiveData<String> _titleText = new MutableLiveData<>();

    public AndroidUIManager(){
        for (int i = 0; i < 4; i++) {
            _qwerTexts[i] = new MutableLiveData<>();
            _qwerEnables[i] = new MutableLiveData<>();
        }
    }

    @Override
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

    @Override
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

    @Override
    public void invoke(int port){
        Runnable func = _callbackMapping.get(port);
        if (func != null) {
            func.run();
        }
    }

    @Override
    public void registerCallback(int port, Runnable func){
        Objects.requireNonNull(func);
        _callbackMapping.put(port, func);
    }

    @Override
    public void setTitle(String title){
        _titleText.postValue(title);
    }

    @Override
    public void setTopText(String text){
        _topText.postValue(text);
    }

    @Override
    public void setButtonText(int button, String text) {
        _qwerTexts[button - BUTTON_Q].postValue(text);
    }

    @Override
    public void setButtonActive(int button, boolean active) {
        _qwerEnables[button - BUTTON_Q].postValue(active);
    }

    public MutableLiveData<String> getButtonString(int button){
        switch (button){
            case BUTTON_Q:
            case BUTTON_W:
            case BUTTON_E:
            case BUTTON_R:
                return _qwerTexts[button - BUTTON_Q];
        }
        return null;
    }

    public MutableLiveData<Boolean> getButtonEnabled(int button){
        switch (button){
            case BUTTON_Q:
            case BUTTON_W:
            case BUTTON_E:
            case BUTTON_R:
                return _qwerEnables[button - BUTTON_Q];
        }
        return null;
    }

    public MutableLiveData<String> getTopText(){
        return _topText;
    }

    public MutableLiveData<String> getTitleText(){
        return _titleText;
    }
}
