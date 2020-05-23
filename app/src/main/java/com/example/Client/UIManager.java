package com.example.Client;

public interface UIManager {
    int ENTER_ROOM_PORT = 0;
    int ROOM_START_PORT = 0;
    int EXIT_ROOM_PORT = 1;
    int BUTTON_Q = 0;
    int BUTTON_W = 1;
    int BUTTON_E = 2;
    int BUTTON_R = 3;

    void switchScreen(ScreenType type, Runnable onComplete);
    void setCurrentScreen(Screen screen, ScreenType type);
    void invoke(int port);
    void registerCallback(int port, Runnable func);
    void setTitle(String title);
    void setTopText(String text);
    void setButtonText(int button, String text);
    void setButtonActive(int button, boolean active);
}
