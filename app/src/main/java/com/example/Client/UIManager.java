package com.example.Client;

import Common.Skill;

public interface UIManager {
    int ROOM_START_PORT = 0;
    int BUTTON_Q = 0;
    int BUTTON_W = 1;
    int BUTTON_E = 2;
    int BUTTON_R = 3;

    void update(long ms);
    void switchScreen(ScreenType type, Runnable onComplete);
    void setCurrentScreen(Screen screen, ScreenType type);
    void invoke(int port);
    void registerCallback(int port, Runnable func);
    void setTitle(String title);
    String getDefaultTopText();
    void setDefaultTopText(String text);
    void setTopText(String text);
    void setTopText(String text, float seconds);
    void setButtonText(int button, String text);
    void setButtonActive(int button, boolean active);
    void setHealth(int health);
    void updateItems();
    int findButtonIndex(Skill skill);

    void failConnection();
}
