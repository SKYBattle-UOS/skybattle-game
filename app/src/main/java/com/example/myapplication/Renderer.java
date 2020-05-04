package com.example.myapplication;

import android.util.Log;

/**
 * 그리기 연산을 하고 싶을 때 사용하는 클래스.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class Renderer {
    /**
     * 채우기가 있는 원을 지도에 그립니다.
     * @param latitude 위도
     * @param longitude 경도
     * @param color 채우기 색
     * @param size 크기
     */
    public void drawFilledCircle(double latitude, double longitude, int color, float size){
        // TODO: DEBUG EDIT
//        Log.i("Stub",
//                String.format(
//                        "Renderer: batched filled #%X circle at %f, %f with size %f",
//                        color, latitude, longitude, size));
    }

    /**
     * 채우기가 없는 원을 지도에 그립니다.
     * @param latitude 위도
     * @param longitude 경도
     * @param color 테두리 색
     * @param weight 태두리 굵기
     * @param radius 반지름
     */
    public void drawCircle(double latitude, double longitude, int color, float weight, float radius){
        // TODO: DEBUG EDIT
//        Log.i("Stub",
//                String.format(
//                        "Renderer: batched #%X circle at %f, %f with radius %f and weight %f",
//                        color, latitude, longitude, radius, weight));
    }

    public void render(long ms){
        // TODO: DEBUG EDIT
//        Log.i("Stub", "Renderer: Rendered batched render calls");
    }
}
