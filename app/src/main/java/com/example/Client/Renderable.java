package com.example.Client;

public interface Renderable {
    void render(Renderer renderer);
    RenderComponent getRenderComponent();
    void setRenderComponent(RenderComponent renderComponent);
}
