package com.example.Client;

/**
 * 앱에서 지도 상황을 보여주는 인터페이스
 *
 * @author hyunji
 * @version 0.0
 * @since 2020-04-28
 */
public interface Map {
    void moveCamera(double latitude, double longitude);
    MapMarkerHandle addMarker(double latitude, double longitude, int color, float size);
    void setMarkerPosition(MapMarkerHandle marker, double lat, double lon);
}
