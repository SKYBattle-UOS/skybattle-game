package com.example.Client;

import Common.GameState;
import Common.Player;

public class PlayerStateBase implements GameState {
    protected Player _player;

    public PlayerStateBase(Player player){
        _player = player;
    }
}
