package com.example.Client;

import android.graphics.Color;

import Common.GameObject;

class MapMarkerRenderComponent implements MapRenderComponent {
    private GameObject _parent;
    private MapMarkerHandle _marker;
    private double[] _prevPosition;

    public MapMarkerRenderComponent(GameObject parent) {
        _parent = parent;
        _prevPosition = new double[2];
    }

    @Override
    public void render(Map map, long ms) {
        double[] position = _parent.getPosition();
        if (_marker == null)
            _marker = map.addMarker(position[0], position[1], Color.YELLOW, 10);

        if (_prevPosition[0] != position[0] || _prevPosition[1] != position[1]) {
            map.setMarkerPosition(_marker, position[0], position[1]);
            _prevPosition = position;
        }
    }
}
