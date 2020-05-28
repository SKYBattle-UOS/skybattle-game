package com.example.Client;

/**
 * 앱에서 지도 상황을 보여주는 인터페이스
 *
 * @author hyunji
 * @version 0.0
 * @since 2020-04-28
 */
public interface Map {
    MapCircleHandle addCircle(double lat, double lon, int color, float radius);
    MapMarkerHandle addMarker(double latitude, double longitude, int color, float size, String name);

    void moveCamera(double latitude, double longitude);
    void zoomCamera(float zoom);
    void setMarkerPosition(MapMarkerHandle marker, double lat, double lon);
    void setCirclePosition(MapCircleHandle circle, double lat, double lon);
    void setCircleRadius(MapCircleHandle circle, float radius);
    void removeMarker(MapMarkerHandle marker);
    void removeCircle(MapCircleHandle circle);
}
