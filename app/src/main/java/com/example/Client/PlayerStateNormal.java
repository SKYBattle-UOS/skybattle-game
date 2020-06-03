package com.example.Client;

import Common.Player;

public class PlayerStateNormal extends PlayerStateBase {
    public PlayerStateNormal(Player player) {
        super(player);
    }

    @Override
    public void start() {
        if (Core.get().getMatch().getThisPlayer() == _player){
            setButtons();
            Core.get().getUIManager().setHealth(_player.getProperty().getHealth());
        }
    }

    @Override
    public void update(long ms) {

    }

    private void setButtons(){
        for (int i = 0; i < 4; i++){
            Core.get().getUIManager()
                    .setButtonText(UIManager.BUTTON_Q + i,
                            _player.getProperty().getSkills().get(i).getName());
            Core.get().getUIManager().setButtonActive(UIManager.BUTTON_Q + i, true);
        }
    }
}
