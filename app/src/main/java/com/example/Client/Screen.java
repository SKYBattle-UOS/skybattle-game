package com.example.Client;

enum ScreenType {
    MAIN,
    ROOM,
    ASSEMBLE,
    CHARACTERSELECT,
    GETREADY,
    INGAME,
    DEATH,
    END
}

public interface Screen {
    void switchTo(ScreenType type);
}
