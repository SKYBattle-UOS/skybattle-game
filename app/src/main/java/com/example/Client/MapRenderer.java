package com.example.Client;

import java.util.ArrayList;

import Common.Camera;
import Common.GameObject;

public class MapRenderer implements Renderer, Camera {
    private Map _map;
    private ArrayList<RenderComponent> _batched;

    MapRenderer(Map map){
        _map = map;
        _batched = new ArrayList<>();
    }

    @Override
    public void render(long ms) {
        for (RenderComponent comp : _batched)
            comp.render(ms);

        _batched.clear();
    }

    @Override
    public void batch(RenderComponent component) {
        _batched.add(component);
    }

    @Override
    public RenderComponent createRenderComponent(GameObject parent, ImageType type) {
        switch (type){
            case FILLED_CIRCLE:
                return new MapMarkerRenderComponent(_map, parent);
            case HOLLOW_CIRCLE:
                return new MapCircleRenderComponent(_map, parent);
        }
        return null;
    }

    @Override
    public void move(double x, double y) {
        _map.moveCamera(x, y);
    }

    @Override
    public void zoom(float scale) {
        _map.zoomCamera(scale);
    }
}
