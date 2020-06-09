package com.example.Client;

import android.graphics.Color;

import Common.GameObject;

class MapMarkerRenderComponent implements RenderComponent {
    private Map _map;
    private GameObject _parent;
    private MapMarkerHandle _marker;
    private double[] _prevPosition;
    private String _prevName;

    public MapMarkerRenderComponent(Map map, GameObject parent) {
        _map = map;
        _parent = parent;
        _prevPosition = new double[2];
        _marker = _map.addMarker(_parent.getPosition()[0], _parent.getPosition()[1],
                Color.YELLOW, 10, _parent.getName());
        _prevName = _parent.getName();
    }

    @Override
    public void render(long ms) {
        double[] position = _parent.getPosition();
        if (_prevPosition[0] != position[0] || _prevPosition[1] != position[1]) {
            _map.setMarkerPosition(_marker, position[0], position[1]);
            _prevPosition = position;
        }

        if (!_prevName.equals(_parent.getName())){
            _map.setMarkerName(_marker, _parent.getName());
            _prevName = _parent.getName();
        }
    }

    @Override
    public void destroy() {
        _map.removeMarker(_marker);
    }
}
