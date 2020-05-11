package com.example.Client;

import Common.GameObject;

public interface Renderer {
    void render(long ms);
    void batch(RenderComponent component);
    RenderComponent createRenderComponent(GameObject parent, ImageType type);
}