package com.example.Client;

import Common.GameObject;
import Common.ImageType;
import Common.InputBitStream;
import Common.Item;
import Common.ItemProperty;

public class ItemClient extends GameObjectClient implements Item {
    ItemProperty _property = new ItemProperty(){
        @Override
        public void setPickedUp(boolean pickedUp) {
            super.setPickedUp(pickedUp);
            if (pickedUp)
                setLook(ImageType.INVISIBLE);
        }
    };

    public ItemClient() {
        _property.setSkill(new HealthUpClient(getMatch(), Core.get().getUIManager()));
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        super.readFromStream(stream, dirtyFlag);
        _property.readFromStream(stream, dirtyFlag, Core.get().getMatch().getRegistry());
    }

    @Override
    public void before(long ms) {

    }

    @Override
    public void update(long ms) {
    }

    @Override
    public void after(long ms) {

    }

    @Override
    public GameObject getGameObject() {
        return this;
    }

    @Override
    public ItemProperty getProperty(){
        return _property;
    }
}
