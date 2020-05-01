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

public interface Screen {
    void switchTo(ScreenType type);
}
