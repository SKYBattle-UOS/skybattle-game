package com.example.Client;

import android.util.Log;

import Common.GameObject;
import Host.HealthUpCommon;

public class HealthUp extends HealthUpCommon {
    @Override
    public void cast(GameObject caster) {
        if (caster == Core.getInstance().getInputManager().getThisPlayer())
            Log.i("hehe", "helth up");
    }
}
