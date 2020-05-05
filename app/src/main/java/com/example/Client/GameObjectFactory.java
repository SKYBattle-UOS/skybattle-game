package com.example.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class GameObjectFactory {
    private int _nextClassId;
    private Map<Integer, Supplier<GameObject>> _mappingC2F = new HashMap<>();

    public GameObject createGameObject(int classId){
        return Objects.requireNonNull(_mappingC2F.get(classId)).get();
    }

    public int registerCreateMethod(Supplier<GameObject> factoryMethod){
        _mappingC2F.put(_nextClassId, factoryMethod);
        return _nextClassId++;
    }
}
