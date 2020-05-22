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
        double[] position = _parent.getPosition();
        if (_circle == null) {
            _circle = _map.addCircle(position[0], position[1], Color.RED, _parent.getRadius());
            _prevPosition = position;
        }

        if (_prevPosition[0] != position[0] || _prevPosition[1] != position[1]) {
            _map.setCirclePosition(_circle, position[0], position[1]);
            _prevPosition = position;
        }
    }

    @Override
    public void destroy() {
        _map.removeCircle(_circle);
    }
}
