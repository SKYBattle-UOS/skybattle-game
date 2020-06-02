package com.example.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import Common.GameObject;
import Common.Skill;

public class GameObjectFactory {
    private int _nextClassId;
    private Map<Integer, Supplier<GameObject>> _mappingC2G = new HashMap<>();
    private Map<Integer, Supplier<Skill>> _mappingC2S = new HashMap<>();

    public GameObject createGameObject(int classId){
        if (_mappingC2G.get(classId) == null)
            return null;
        return Objects.requireNonNull(_mappingC2G.get(classId)).get();
    }

    public int registerGameObject(Supplier<GameObject> factoryMethod){
        _mappingC2G.put(_nextClassId, factoryMethod);
        return _nextClassId++;
    }

    public int registerSkill(Supplier<Skill> factoryMethod){
        _mappingC2S.put(_nextClassId, factoryMethod);
        return _nextClassId++;
    }

    public Skill createSkill(int classId){
        if (_mappingC2S.get(classId) == null)
            return null;
        return Objects.requireNonNull(_mappingC2S.get(classId)).get();
    }
}
