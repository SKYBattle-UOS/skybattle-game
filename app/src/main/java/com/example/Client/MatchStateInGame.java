package com.example.Client;

import Common.GameObject;
import Common.GameState;
import Common.ImageType;
import Common.IngameInfoListener;
import Common.InputBitStream;
import Common.MatchStateType;
import Common.ReadOnlyList;
import Common.Skill;
import Common.Util;
import Common.GameOverState;

public class MatchStateInGame implements GameState, IngameInfoListener {
    private GameStateMatch _match;
    private PlayerClient _thisPlayer;
    private String _originalName;

    MatchStateInGame(GameStateMatch gameStateMatch) {
        _match = gameStateMatch;
    }

    @Override
    public void start() {
        _thisPlayer = (PlayerClient) Core.get().getMatch().getThisPlayer();
        _thisPlayer.setIngameInfoListener(this);
        onPlayerStateChange(_thisPlayer.getProperty().getPlayerState());
        Core.get().getUIManager().setDefaultTopText("게임이 시작되었습니다");
        if (_thisPlayer.getProperty().getPlayerState() == PlayerState.ZOMBIE)
            Core.get().getUIManager().setTopText("당신은 좀비입니다", 3f);
    }

    @Override
    public void update(long ms) {
        receiveRemainingTime();
        receiveGameOver();
    }

    private void receiveGameOver() {
        InputBitStream stream = Core.get().getPakcetManager().getPacketStream();
        if (stream == null) return;

        GameOverState state = GameOverState.GOING;
        if (Util.hasMessage(stream)){
            int stateIndex = stream.read(2);
            state = GameOverState.values()[stateIndex];
        }

        if (state != GameOverState.GOING){
            Core.get().getUIManager().setGameOver(state);
            Core.get().getUIManager().switchScreen(ScreenType.GAMEOVER, null);
            _match.switchState(MatchStateType.GAMEOVER);
        }
    }

    private void receiveRemainingTime() {
        InputBitStream stream = Core.get().getPakcetManager().getPacketStream();
        if (stream == null) return;

        if (Util.hasMessage(stream)){
            int remainingSeconds = stream.read(10);
            Core.get().getUIManager().setRemainingTime(remainingSeconds);
        }
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
                setGameUI();
                break;

            case ZOMBIE:
                Core.get().getUIManager().setTopText("좀비가 되었습니다", 3f);
                Core.get().getUIManager().reconstructSkillButtons();
                setGameUI();
                break;

            case GHOST:
                setDeathScreen();
                makeGhost();
                break;
        }
    }

    @Override
    public void onItemsChange() {
        Core.get().getUIManager().updateItems();
    }

    @Override
    public void onHealthChange(int health) {
        Core.get().getUIManager().setHealth(health);
    }

    private void setGameUI(){
        UIManager uiManager = Core.get().getUIManager();
        int i = 0;
        for (Skill skill : _thisPlayer.getProperty().getSkills()){
            uiManager.setButtonText(UIManager.BUTTON_Q + i, skill.getName());
            uiManager.setButtonActive(UIManager.BUTTON_Q + i++, true);
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