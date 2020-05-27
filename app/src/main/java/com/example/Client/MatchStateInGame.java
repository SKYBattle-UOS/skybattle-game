package com.example.Client;

import java.util.Collection;
import java.util.List;

import Common.GameObject;
import Common.GameState;

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

    MatchStateInGame(GameStateMatch gameStateMatch) {
        _match = gameStateMatch;
        Core.getInstance().getUIManager().setDefaultTopText("게임이 시작되었습니다");
    }

    @Override
    public void start() {
        // TODO this is temp
        Player player = null;
        List<GameObject> gos = _match.getWorld();
        for (GameObject go : gos){
            if (go instanceof Player) {
                player = (Player) go;
                Core.getInstance().getInputManager().setThisPlayer((Player) go);
                break;
            }
        }

        setButtons(player, true);
    }

    @Override
    public void update(long ms) {
        InputManager inputManager = Core.getInstance().getInputManager();
        UIManager uiManager = Core.getInstance().getUIManager();

        if (inputManager.getThisPlayer() == null){
            _isPlayerDead = true;
            uiManager.setDefaultTopText("당신은 죽었습니다. 부활지점으로 이동하세요.");
            uiManager.switchScreen(ScreenType.DEATH, null);
            inputManager.setThisPlayer(new Player(0, 0, "temp"));

            List<GameObject> world = _match.getWorld();
            for (GameObject go : world) {
                if (go.getName().equals("부활 지점")) {
                    go.setRenderComponent(
                            Core.getInstance().getRenderer().createRenderComponent(
                                    go, ImageType.CIRCLE_WITH_MARKER
                            )
                    );

                    double[] respawnLatLon = go.getPosition();
                    Core.getInstance().getCamera().move(respawnLatLon[0], respawnLatLon[1]);
                }
            }
        }

        if (_isPlayerDead){
            // TODO update player position
        }
    }

    @Override
    public void render(Renderer renderer, long ms) {
        Collection<GameObject> gameObjects = _match.getWorld();
        for (GameObject go : gameObjects){
            go.render(renderer);
        }
    }

    private void setButtons(Player player, boolean enable){
        for (int i = 0; i < 4; i++){
            if (player != null) {
                Core.getInstance().getUIManager()
                        .setButtonText(UIManager.BUTTON_Q + i, player.getSkills()[i].getName());
            }
            Core.getInstance().getUIManager().setButtonActive(UIManager.BUTTON_Q + i, enable);
        }
    }
}