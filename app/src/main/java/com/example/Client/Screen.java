package com.example.Client;

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
    void setTopText(String text);
}
