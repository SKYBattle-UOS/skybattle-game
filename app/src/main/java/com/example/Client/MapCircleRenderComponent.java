package com.example.Client;

import android.graphics.Color;

import Common.GameObject;

class MapCircleRenderComponent implements RenderComponent {
    private Map _map;
    private GameObject _parent;
    private double[] _prevPosition;
    private MapCircleHandle _circle;

    public MapCircleRenderComponent(Map map, GameObject parent) {
        _map = map;
        _parent = parent;
        _prevPosition = new double[2];
    }

    @Override
    public void render(long ms) {
//        double[] position = _parent.getPosition();
//        if (_circle == null) {
//            _circle = _map.addMarker(position[0], position[1], Color.YELLOW, 10, _parent.getName());
//            _prevPosition = position;
//        }
//
//        if (_prevPosition[0] != position[0] || _prevPosition[1] != position[1]) {
//            _map.setMarkerPosition(_marker, position[0], position[1]);
//            _prevPosition = position;
//        }
    }

    @Override
    public void destroy() {

    }
}
