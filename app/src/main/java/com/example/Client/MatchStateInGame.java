package com.example.Client;

import java.util.Collection;

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

    MatchStateInGame(GameStateMatch gameStateMatch) {
        _match = gameStateMatch;
        Core.getInstance().getUIManager().setDefaultTopText("게임이 시작되었습니다");
    }

    @Override
    public void start() {
        // TODO this is temp
        Player player = null;
        Collection<GameObject> gos = _match.getWorld();
        for (GameObject go : gos){
            if (go instanceof Player) {
                player = (Player) go;
                Core.getInstance().getInputManager().setThisPlayer((Player) go);
                break;
            }
        }

        for (int i = 0; i < 4; i++){
            Core.getInstance().getUIManager().setButtonText(AndroidUIManager.BUTTON_Q + i, player.getSkills()[i].getName());
            Core.getInstance().getUIManager().setButtonActive(AndroidUIManager.BUTTON_Q + i, true);
        }
    }

    @Override
    public void update(long ms) {
        // TODO
    }

    @Override
    public void render(Renderer renderer, long ms) {
        Collection<GameObject> gameObjects = _match.getWorld();
        for (GameObject go : gameObjects){
            go.render(renderer);
        }
    }
}