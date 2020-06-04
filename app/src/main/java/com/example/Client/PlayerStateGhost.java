package com.example.Client;

import Common.GameObject;
import Common.ImageType;
import Common.Player;
import Common.ReadOnlyList;

public class PlayerStateGhost extends PlayerStateBase {
    private String _originalName;

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
    public void finish() {
        tearDownDeathScreen();
        Core.get().getUIManager().switchScreen(ScreenType.INGAME, null);
    }

    private void setDeathScreen() {
        UIManager uiManager = Core.get().getUIManager();
        uiManager.setTopText("당신은 죽었습니다. 부활지점으로 이동하세요.");
        uiManager.switchScreen(ScreenType.MAP, null);

        GameObject respawnArea = findRespawnArea();
        respawnArea.setLook(ImageType.CIRCLE_WITH_MARKER);
        double[] respawnLatLon = respawnArea.getPosition();
        Core.get().getCamera().move(respawnLatLon[0], respawnLatLon[1]);
    }

    private void tearDownDeathScreen(){
        UIManager uiManager = Core.get().getUIManager();
        uiManager.setTopText(uiManager.getDefaultTopText());
        _player.getGameObject().setName(_originalName);

        GameObject respawnArea = findRespawnArea();
        respawnArea.setLook(ImageType.INVISIBLE);
    }

    private void makeGhost() {
        _originalName = _player.getGameObject().getName();
        _player.getGameObject().setLook(ImageType.MARKER);
        _player.getGameObject().setName("유령");
    }

    private GameObject findRespawnArea(){
        ReadOnlyList<GameObject> world = Core.get().getMatch().getWorld();
        for (GameObject go : world)
            if (go.getName().equals("부활지점"))
                return go;

        return null;
    }
}
