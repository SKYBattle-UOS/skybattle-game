package com.example.Client;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.ArrayList;

import Common.GameOverState;
import Common.Item;
import Common.Player;
import Common.RoomUserInfo;
import Common.Skill;
import Common.UIManager;

public class AndroidUIManager implements UIManager, LifecycleObserver {
    private Screen _currentScreen;
    private ScreenType _nextScreenType;
    private Runnable _onComplete;
    private boolean _shouldSwitch;
    private final Object _mutex = new Object();
    private String _defaultTopText;
    private long _timer;

    private Handler _mainHandler = new Handler(Looper.getMainLooper());

    @SuppressWarnings("unchecked")
    private MutableLiveData<String>[] _qwerTexts = new MutableLiveData[4];
    @SuppressWarnings("unchecked")
    private MutableLiveData<Boolean>[] _qwerEnables = new MutableLiveData[4];
    private MutableLiveData<String> _topText = new MutableLiveData<>();
    private MutableLiveData<String> _titleText = new MutableLiveData<>();
    private MutableLiveData<Integer> _health = new MutableLiveData<>();
    private MutableLiveData<ArrayList<RoomUserInfo>> _roomInfos = new MutableLiveData<>();
    private MutableLiveData<Integer> _remainingTime = new MutableLiveData<>();
    private MutableLiveData<GameOverState> _gameOverState = new MutableLiveData<>();

    private InGameFragment _ingameFrag;

    public AndroidUIManager(){
        for (int i = 0; i < 4; i++) {
            _qwerTexts[i] = new MutableLiveData<>();
            _qwerEnables[i] = new MutableLiveData<>();
        }

        _roomInfos.postValue(new ArrayList<>());
    }

    @Override
    public void update(long ms) {
        if (_timer > 0){
            _timer -= ms;
            if (_timer <= 0){
                _topText.postValue(_defaultTopText);
            }
        }
    }

    @Override
    public void switchScreen(ScreenType type, Runnable onComplete){
        if (_shouldSwitch) return;

        _nextScreenType = type;
        _onComplete = onComplete;
        _shouldSwitch = true;

        postSwitchScreen(type);
    }

    private void postSwitchScreen(ScreenType type) {
        _mainHandler.post(() -> {
            synchronized (_mutex){
                if (_currentScreen != null){
                    _shouldSwitch = false;
                    _currentScreen.switchTo(type);
                }
            }
        });
    }

    @Override
    public void setCurrentScreen(Screen screen, ScreenType type){
        synchronized (_mutex){
            _currentScreen = screen;
            if (_currentScreen == null) return;

            if (_shouldSwitch) {
                postSwitchScreen(_nextScreenType);
                return;
            }

            if (type == _nextScreenType){
                if (_onComplete != null)
                    _onComplete.run();
                _nextScreenType = null;
                _onComplete = null;
            }
        }
    }

    @Override
    public void setTitle(String title){
        _titleText.postValue(title);
    }

    @Override
    public String getDefaultTopText() {
        return _defaultTopText;
    }

    @Override
    public void setDefaultTopText(String text){
        _defaultTopText = text;
        _topText.postValue(text);
    }

    @Override
    public void setTopText(String text){
        _topText.postValue(text);
    }

    @Override
    public void setTopText(String text, float seconds) {
        _timer = (long )(seconds * 1000);
        _topText.postValue(text);
    }

    @Override
    public void setButtonText(int button, String text) {
        if (button < 0) return;

        if (button - BUTTON_Q < 4)
            _qwerTexts[button - BUTTON_Q].postValue(text);
    }

    @Override
    public void setButtonActive(int button, boolean active) {
        if (button < 0) return;

        if (button - BUTTON_Q < 4)
            _qwerEnables[button - BUTTON_Q].postValue(active);
    }

    @Override
    public void setHealth(int health) {
        _health.postValue(health);
    }

    @Override
    public void setRoomUserInfos(ArrayList<RoomUserInfo> roomInfos) {
        _roomInfos.postValue(roomInfos);
    }

    @Override
    public void setRemainingTime(int seconds) {
        _remainingTime.postValue(seconds);
    }

    @Override
    public void setGameOver(GameOverState state) {
        _gameOverState.postValue(state);
    }

    @Override
    public void updateItems() {
        Player thisPlayer = Core.get().getMatch().getThisPlayer();

        _mainHandler.post(() -> {
            if (_ingameFrag == null) return;
            _ingameFrag.clearItemButtons();
            int i = 4;
            for (Item item : thisPlayer.getGameObject().getItems()){
                int finalI = i;
                _ingameFrag.addItemButton(btn -> {
                    btn.setText(item.getGameObject().getName());
                    _ingameFrag.setButtonListener(item.getProperty().getSkill(), btn, finalI);
                });
                i++;
            }
        });
    }

    @Override
    public void reconstructSkillButtons() {
        _mainHandler.post(() -> {
            _ingameFrag.clearSkillButtons();
            _ingameFrag.addSkillButtons();
        });
    }

    @Override
    public int findButtonIndex(Skill skill) {
        Player thisPlayer = Core.get().getMatch().getThisPlayer();
        int index = thisPlayer.getProperty().getSkills().indexOf(skill);
        if (index >= 0)
            return UIManager.BUTTON_Q + index;

        return -1;
    }

    @Override
    public void failConnection() {
        _mainHandler.post(() -> ((MainScreen) _currentScreen).onHostNotFound());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(LifecycleOwner owner){
        // TODO: should lock
        _ingameFrag = (InGameFragment) owner;
        updateItems();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(LifecycleOwner owner){
        _ingameFrag = null;
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

    public MutableLiveData<Integer> getHealth() { return _health; }

    public MutableLiveData<ArrayList<RoomUserInfo>> getRoomUserInfos(){ return _roomInfos; }

    public MutableLiveData<Integer> getRemainingTime() { return _remainingTime; }

    public MutableLiveData<GameOverState> getGameOverState() { return _gameOverState; }
}
