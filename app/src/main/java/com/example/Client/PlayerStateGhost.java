package com.example.Client;

import Common.GameObject;
import Common.ImageType;
import Common.Player;
import Common.ReadOnlyList;

public class PlayerStateGhost extends PlayerStateBase {
    public PlayerStateGhost(Player player) {
        super(player);
    }

    @Override
    public void start() {
        if (_player == Core.get().getMatch().getThisPlayer()){
            setDeathScreen();
            makeGhost();
        }
    }

    @Override
    public void update(long ms) {

    }

    private void setDeathScreen() {
        UIManager uiManager = Core.get().getUIManager();
        uiManager.setTopText("당신은 죽었습니다. 부활지점으로 이동하세요.");
        uiManager.switchScreen(ScreenType.MAP, null);

        ReadOnlyList<GameObject> world = Core.get().getMatch().getWorld();
        for (GameObject go : world) {
            if (go.getName().equals("부활지점")) {
                go.setLook(ImageType.CIRCLE_WITH_MARKER);

                double[] respawnLatLon = go.getPosition();
                Core.get().getCamera().move(respawnLatLon[0], respawnLatLon[1]);
            }
        }
    }

    private void makeGhost() {
        _player.getGameObject().setLook(ImageType.MARKER);
        _player.getGameObject().setName("현재위치");
    }
}
