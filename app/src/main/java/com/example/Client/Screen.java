package com.example.Client;

enum ScreenType {
    MAIN,
    ROOM,
    MAP,
    CHARACTERSELECT,
    INGAME,
    GAMEOVER
}

public interface Screen {
    void switchTo(ScreenType type);
}
