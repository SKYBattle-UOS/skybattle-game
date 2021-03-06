package Common;

import com.example.Client.Screen;
import com.example.Client.ScreenType;

import java.util.ArrayList;

import Common.GameOverState;
import Common.RoomUserInfo;
import Common.Skill;

public interface UIManager {
    int BUTTON_Q = 0;
    int BUTTON_W = 1;
    int BUTTON_E = 2;
    int BUTTON_R = 3;

    void update(long ms);
    void switchScreen(ScreenType type, Runnable onComplete);
    void setCurrentScreen(Screen screen, ScreenType type);
    void setTitle(String title);
    String getDefaultTopText();
    void setDefaultTopText(String text);
    void setTopText(String text);
    void setTopText(String text, float seconds);
    void setButtonText(int button, String text);
    void setButtonActive(int button, boolean active);
    void setHealth(int health);
    void setRoomUserInfos(ArrayList<RoomUserInfo> roomInfos);
    void setRemainingTime(int seconds);
    void setGameOver(GameOverState state);
    void updateItems();
    void reconstructSkillButtons();
    int findButtonIndex(Skill skill);

    void failConnection();
}
