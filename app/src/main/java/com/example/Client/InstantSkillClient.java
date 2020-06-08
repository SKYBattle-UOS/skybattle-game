package com.example.Client;

import Common.InstantSkill;

public abstract class InstantSkillClient extends InstantSkill {
    private int _coolTime = 10;

    protected void runCoolTime(int seconds) {
        _coolTime = seconds;
        _runCoolTime();
    }

    private void _runCoolTime(){
        int buttonIndex = Core.get().getUIManager().findButtonIndex(this);
        if (_coolTime > 0){
            Core.get().getUIManager().setButtonText(buttonIndex, String.valueOf(_coolTime));
            Core.get().getMatch().setTimer(this::_runCoolTime, 1);
            _coolTime--;
        }
        else {
            Core.get().getUIManager().setButtonActive(buttonIndex, true);
            Core.get().getUIManager().setButtonText(buttonIndex, getName());
        }
    }
}
