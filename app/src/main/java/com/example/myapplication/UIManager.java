package com.example.myapplication;

enum ScreenType {
    MAIN,
    ROOM,
    ASSEMBLE,
    CHARACTERSELECT,
    GETREADY,
    INGAME,
    END
}

public class UIManager {
    public void switchScreen(ScreenType type){
        // TODO
        Screen screen = null; // 자료구조에서 스크린을 가져온다.
        if (screen == null); // 스크린이 없으면 기록해뒀다가 스크린이 등록될 때 바꾼다. (가장 최근에 요청된걸로)


    }

    public void registerScreen(ScreenType type, Screen screen){
        // TODO
    }
}
