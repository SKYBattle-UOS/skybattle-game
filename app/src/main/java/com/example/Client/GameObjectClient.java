package com.example.Client;

import Common.GameObject;
import Common.ImageType;

public abstract class GameObjectClient extends GameObject implements Renderable {
    private RenderComponent _renderComponent;

    protected GameObjectClient(float latitude, float longitude, String name) {
        super(latitude, longitude, name);
    }

    @Override
    public void faceDeath() {
        super.faceDeath();
        _renderComponent.destroy();
    }

    @Override
    public void setLook(ImageType type) {
        super.setLook(type);
        setRenderComponent(Core.get().getRenderer().createRenderComponent(this, type));
    }

    @Override
    public void render(Renderer renderer){
        renderer.batch(_renderComponent);
    }

    @Override
    public RenderComponent getRenderComponent() {
        return _renderComponent;
    }

    @Override
    public void setRenderComponent(RenderComponent renderComponent) {
        if (_renderComponent != null)
            _renderComponent.destroy();
        this._renderComponent = renderComponent;
    }
}
