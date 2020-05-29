package com.example.Client;

import Common.GameObject;
import Common.ImageType;

public interface Renderer {
    void render(long ms);
    void batch(RenderComponent component);
    RenderComponent createRenderComponent(GameObject parent, ImageType type);
}