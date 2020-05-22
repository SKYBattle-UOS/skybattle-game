package com.example.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Common.DynamicLookChangerCommon;
import Common.GameObject;

public class DynamicLookChanger extends DynamicLookChangerCommon {
    private HashMap<Integer, ImageType> _tryLater = new HashMap<>();
    private ArrayList<Integer> _toRemove = new ArrayList<>();

    protected DynamicLookChanger(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    public static GameObject createInstance(){
        return new DynamicLookChanger(0, 0, "DynamicLookChanger");
    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
        for (Map.Entry<Integer, ImageType> entry : _nid2newImageType.entrySet()){
            GameObject go = _match.getRegistry().getGameObject(entry.getKey());
            if (go == null){
                _tryLater.put(entry.getKey(), entry.getValue());
                continue;
            }
            go.setRenderComponent(Core.getInstance().getRenderer().createRenderComponent(go, entry.getValue()));
        }

        _nid2newImageType.clear();

        for (Map.Entry<Integer, ImageType> entry : _tryLater.entrySet()){
            GameObject go = _match.getRegistry().getGameObject(entry.getKey());
            if (go == null){
                continue;
            }
            _toRemove.add(entry.getKey());
            go.setRenderComponent(Core.getInstance().getRenderer().createRenderComponent(go, entry.getValue()));
        }

        for (int i : _toRemove)
            _tryLater.remove(i);
        _toRemove.clear();
    }

    @Override
    public void after(long ms) {

    }
}
