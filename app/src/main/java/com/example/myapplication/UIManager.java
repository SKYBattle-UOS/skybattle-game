package com.example.myapplication;

public class UIManager {
    Screen _currentScreen;
    ScreenType _screenToSwitch;

    // UIManager를 activity에 대한 lifecycle-aware component로 만들면 더 좋을 겉 같습니다.
    public void switchScreen(ScreenType type){
        // TODO
        if (_currentScreen == null){
            _screenToSwitch = type; // 스크린이 없으면 기록해뒀다가 스크린이 등록될 때 바꾼다. (가장 최근에 요청된걸로)
            return;
        }

        _currentScreen.switchTo(type);
        _screenToSwitch = null;
    }

    public void setCurrentScreen(Screen screen){
        _currentScreen = screen;

        // TODO
        if (_screenToSwitch != null); // 스크린이 준비가 되면 바꾸라고 해야한다. (onCreate에서 바꾸면 안됨)
    }
}
