package com.example.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Common.GameObject;

public class DynamicLookChanger extends GameObject {
    private HashMap<Integer, ImageType> _tryLater = new HashMap<>();
    private ArrayList<Integer> _toRemove = new ArrayList<>();

    protected DynamicLookChanger(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

//    public static GameObject createInstance(){
////        return new DynamicLookChanger(0, 0, "DynamicLookChanger");
//    }

//    @Override
//    public void readFromStream(InputBitStream stream, int dirtyFlag) {
//        super.readFromStream(stream, dirtyFlag);
//
//        if ((dirtyFlag & (1 << _dirtyPos++)) != 0) {
//            int len = stream.read(8);
//            for (int i = 0; i < len; i++){
//                int networkId = stream.read(32);
//                int imageType = stream.read(4);
//                GameObject go = _match.getRegistry().getGameObject(networkId);
//                if (go == null){
//                    _tryLater.put(networkId, ImageType.values()[imageType]);
//                    continue;
//                }
//                go.setRenderComponent(Core.getInstance().getRenderer().createRenderComponent(go, ImageType.values()[imageType]));
//            }
//        }
//    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
        if (_tryLater.isEmpty()) return;

        for (Map.Entry<Integer, ImageType> entry : _tryLater.entrySet()){
            GameObject go = _match.getRegistry().getGameObject(entry.getKey());
            if (go == null){
                continue;
            }
            _toRemove.add(entry.getKey());
            go.setRenderComponent(Core.get().getRenderer().createRenderComponent(go, entry.getValue()));
        }

        for (int i : _toRemove)
            _tryLater.remove(i);
        _toRemove.clear();
    }

    @Override
    public void after(long ms) {

    }
}
