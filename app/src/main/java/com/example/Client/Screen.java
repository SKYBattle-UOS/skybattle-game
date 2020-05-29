package com.example.Client;

enum ScreenType {
    MAIN,
    ROOM,
    MAP,
    CHARACTERSELECT,
    INGAME,
    END
}

public interface Screen {
    void switchTo(ScreenType type);
}
