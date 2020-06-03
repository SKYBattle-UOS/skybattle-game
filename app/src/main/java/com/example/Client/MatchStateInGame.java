package com.example.Client;

import java.util.Collection;
import java.util.List;

import Common.GameObject;
import Common.GameState;
import Common.ImageType;
import Common.Player;
import Common.ReadOnlyList;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 매치 진행 중 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateInGame implements GameState {
    private GameStateMatch _match;
    private boolean _isPlayerDead;
    private boolean _waiting = true;
    private boolean _isDeathScreen;

    MatchStateInGame(GameStateMatch gameStateMatch) {com
        _match = gameStateMatch;
        Core.get().getUIManager().setDefaultTopText("게임이 시작되었습니다");
    }

    @Override
    public void start() {
        Player player = Core.get().getMatch().getThisPlayer();
        player.getGameObject().setOnDeathListener(() -> _isPlayerDead = true);
        setButtons(player, true);
        Core.get().getUIManager().setHealth(player.getProperty().getHealth());
        Core.get().getUIManager().switchScreen(ScreenType.INGAME, () -> _waiting = false);
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;

        if (_isPlayerDead){
            if (!_isDeathScreen) {
                setDeathScreen();
                _isDeathScreen = true;
            }

            if (setGhost())
                _isPlayerDead = false;
        }
    }

    private boolean setGhost() {
        GameObject player = Core.get().getMatch().getThisPlayer().getGameObject();
        if (player != null && player.getName().equals("현재위치")){
            player.setLook(ImageType.MARKER);
            return true;
        }
        return false;
    }

    private void setDeathScreen() {
        UIManager uiManager = Core.get().getUIManager();
        uiManager.setDefaultTopText("당신은 죽었습니다. 부활지점으로 이동하세요.");
        uiManager.switchScreen(ScreenType.MAP, null);

        ReadOnlyList<GameObject> world = _match.getWorld();
        for (GameObject go : world) {
            if (go.getName().equals("부활지점")) {
                go.setLook(ImageType.CIRCLE_WITH_MARKER);

                double[] respawnLatLon = go.getPosition();
                Core.get().getCamera().move(respawnLatLon[0], respawnLatLon[1]);
            }
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        ReadOnlyList<GameObject> gameObjects = _match.getWorld();
        for (GameObject go : gameObjects){
            ((Renderable) go).render(renderer);
        }
    }

    private void setButtons(Player player, boolean enable){
        for (int i = 0; i < 4; i++){
            if (player != null) {
                Core.get().getUIManager()
                        .setButtonText(UIManager.BUTTON_Q + i,
                                player.getProperty().getSkills().get(i).getName());
            }
            Core.get().getUIManager().setButtonActive(UIManager.BUTTON_Q + i, enable);
        }
    }
}