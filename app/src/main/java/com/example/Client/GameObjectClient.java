package com.example.Client;

import java.nio.charset.StandardCharsets;
import java.util.List;

import Common.GameObject;
import Common.ImageType;
import Common.InputBitStream;
import Common.Item;

public abstract class GameObjectClient extends GameObject implements Renderable {
    private RenderComponent _renderComponent;

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        if ((dirtyFlag & posDirtyFlag) != 0) {
            int lat = stream.read(32);
            int lon = stream.read(32);
            _match.getConverter().restoreLatLon(lat, lon, _restoreTemp);
            setPosition(_restoreTemp[0], _restoreTemp[1]);
        }

        if ((dirtyFlag & nameDirtyFlag) != 0) {
            int len = stream.read(8);
            byte[] b = new byte[len];
            stream.read(b, len * 8);
            setName(new String(b, StandardCharsets.UTF_8));
        }

        if ((dirtyFlag & radiusDirtyFlag) != 0) {
            int rInt = stream.read(16);
            setRadius(((float) rInt) / 10f);
        }

        if ((dirtyFlag & imageTypeDirtyFlag) != 0) {
            ImageType type = ImageType.values()[stream.read(4)];
            setLook(type);
        }

        if ((dirtyFlag & itemsDirtyFlag) != 0){
            getItems().clear();
            int itemsSize = stream.read(4);
            for (int i = 0; i < itemsSize; i++){
                GameObject item = _match.getRegistry().getGameObject(stream.read(32));
                getItems().add((Item) item);
            }
            onItemsDirty();
        }
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

    public void onItemsDirty(){}
}
