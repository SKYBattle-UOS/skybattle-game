package com.example.myapplication;

import android.util.Log;

public class WorldSetter {
    private GameObjectRegistry _registry;

    public WorldSetter(GameObjectRegistry registry){
        _registry = registry;
    }

    public void processInstructions(InputBitStream stream){
        // TODO
        WorldSetterHeader header = new WorldSetterHeader();
        header.setMembers(stream);

        switch (header.action){
            case CREATE:
                GameObject newGO = Core.getInstance().getGameObjectFactory().createGameObject(header.classId);
                _registry.add(header.networkId, newGO);
                break;
            case UPDATE:
                break;
            case DESTROY:
                break;
            default:
                break;
        }
    }
}
