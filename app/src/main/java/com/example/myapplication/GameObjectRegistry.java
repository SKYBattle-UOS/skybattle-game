package com.example.myapplication;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameObjectRegistry {
    private Map<Integer, GameObject> _mappingN2G = new HashMap<Integer, GameObject>();
    private Map<GameObject, Integer> _mappingG2N = new HashMap<GameObject, Integer>();

    public void add(int networkId, GameObject go){
        _mappingN2G.put(networkId, go);
        _mappingG2N.put(go, networkId);
    }

    public void remove(int networkId){
        _mappingG2N.remove(_mappingN2G.get(networkId));
        _mappingN2G.remove(networkId);
    }

    public int getId(GameObject go){
        return _mappingG2N.get(go);
    }

    public GameObject getGameObject(int networkId){
        return _mappingN2G.get(networkId);
    }

    public Collection<GameObject> getGameObjects(){
        return _mappingN2G.values();
    }
}
