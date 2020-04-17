package com.example.myapplication;

import android.util.Log;

public class Renderer {
    public void drawFilledCircle(float latitude, float longitude, int color, float size){
        Log.i("Stub",
                String.format(
                        "Renderer: drew filled #%X circle at %f, %f with size %f",
                        color, latitude, longitude, size));
    }

    public void drawCircle(float latitude, float longitude, int color, float weight, float radius){
        Log.i("Stub",
                String.format(
                        "Renderer: drew #%X circle at %f, %f with radius %f and weight %f",
                        color, latitude, longitude, radius, weight));
    }
}
