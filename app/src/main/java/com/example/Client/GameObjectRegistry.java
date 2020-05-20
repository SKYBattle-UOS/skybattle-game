package com.example.Client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Common.GameObject;

public class GameObjectRegistry {
    private Map<Integer, GameObject> _mappingN2G = new HashMap<Integer, GameObject>();

    public void add(int networkId, GameObject go){
        _mappingN2G.put(networkId, go);
    }

    public void remove(int networkId){
        _mappingN2G.remove(networkId);
    }

    public GameObject getGameObject(int networkId){
        if (networkId == 0) return null;
        return _mappingN2G.get(networkId);
    }

    public Collection<GameObject> getGameObjects(){
        return _mappingN2G.values();
    }
}
