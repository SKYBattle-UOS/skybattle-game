package com.example.Client;

public interface MatchScreen extends Screen {
    void setTopText(String text);
    void setButtonText(int button, String text);
    void setButtonActive(int button, boolean active);
}
