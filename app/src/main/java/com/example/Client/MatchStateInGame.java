package com.example.Client;

import Common.GameObject;
import Common.GameState;
import Common.ImageType;
import Common.IngameInfoListener;
import Common.ReadOnlyList;

public class MatchStateInGame implements GameState, IngameInfoListener {
    private GameStateMatch _match;
    private PlayerClient _thisPlayer;
    private boolean _waiting = true;
    private String _originalName;
    private PlayerState _playerState;

    MatchStateInGame(GameStateMatch gameStateMatch) {
        _match = gameStateMatch;
    }

    @Override
    public void start() {
        _thisPlayer = (PlayerClient) Core.get().getMatch().getThisPlayer();
        _thisPlayer.setIngameInfoListener(this);
        Core.get().getUIManager().setDefaultTopText("게임이 시작되었습니다");
        Core.get().getUIManager().switchScreen(ScreenType.INGAME, () -> _waiting = false);
    }

    @Override
    public void update(long ms) {
        if (_waiting) return;
    }


    @Override
    public void render(Renderer renderer, long ms) {
        ReadOnlyList<GameObject> gameObjects = _match.getWorld();
        for (GameObject go : gameObjects){
            ((Renderable) go).render(renderer);
        }
    }

    @Override
    public void onPlayerStateChange(PlayerState state) {
        switch (state){
            case NORMAL:
                if (_playerState == PlayerState.GHOST)
                    tearDownDeathScreen();
                setGameUI();
                break;

            case GHOST:
                setDeathScreen();
                makeGhost();
                break;
        }

        _playerState = state;
    }

    @Override
    public void onItemsChange() {
        Core.get().getUIManager().updateItems();
    }

    private void setGameUI(){
        UIManager uiManager = Core.get().getUIManager();
        for (int i = 0; i < 4; i++){
            uiManager.setButtonText(UIManager.BUTTON_Q + i,
                            _thisPlayer.getProperty().getSkills().get(i).getName());
            uiManager.setButtonActive(UIManager.BUTTON_Q + i, true);
        }
        uiManager.setHealth(_thisPlayer.getProperty().getHealth());
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
        _thisPlayer.setName(_originalName);

        GameObject respawnArea = findRespawnArea();
        respawnArea.setLook(ImageType.INVISIBLE);
    }

    private void makeGhost() {
        _originalName = _thisPlayer.getName();
        _thisPlayer.setLook(ImageType.MARKER);
        _thisPlayer.setName("유령");
    }

    private GameObject findRespawnArea(){
        ReadOnlyList<GameObject> world = Core.get().getMatch().getWorld();
        for (GameObject go : world)
            if (go.getName().equals("부활지점"))
                return go;

        return null;
    }
}