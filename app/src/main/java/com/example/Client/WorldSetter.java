package com.example.Client;

import java.util.Vector;

import Common.GameObject;
import Common.InputBitStream;
import Common.LatLonByteConverter;
import Common.WorldSetterHeader;

public class WorldSetter {
    private GameObjectRegistry _registry;
    private WorldSetterHeader _header;
    private byte[] _buffer;
    private Vector<GameObject> _world;
    LatLonByteConverter _converter;

    public WorldSetter(Vector<GameObject> world, GameObjectRegistry registry, LatLonByteConverter converter){
        _registry = registry;
        _header = new WorldSetterHeader();
        _buffer = new byte[1];
        _world = world;
        _converter = converter;
    }

    public void processInstructions(InputBitStream stream){
        while (true){
            if (stream.read(1) == 0) return;

            _header.readFromStream(stream);

            switch (_header.action){
                case CREATE:
                    createGO(stream);
                    break;
                case UPDATE:
                    updateGO(stream);
                    break;
                case DESTROY:
                    destroyGO();
                    break;
                default:
                    // do nothing
                    break;
            }
        }
    }

    private void createGO(InputBitStream stream){
        if (_registry.getGameObject(_header.networkId) == null){
            GameObject newGO = Core.getInstance().getGameObjectFactory().createGameObject(_header.classId);
            newGO.setGameObjectRegistry(_registry);
            newGO.setLatLonByteConverter(_converter);
            newGO.setNetworkId(_header.networkId);
            newGO.readFromStream(stream, _header.dirtyFlag);
            _registry.add(_header.networkId, newGO);
            newGO.setIndexInWorld(_world.size());
            _world.add(newGO);
        }
    }

    private void updateGO(InputBitStream stream){
        GameObject goToUpdate = _registry.getGameObject(_header.networkId);
        if (goToUpdate == null) return;

        goToUpdate.readFromStream(stream, _header.dirtyFlag);
    }

    private void destroyGO(){
        GameObject goToDestroy = _registry.getGameObject(_header.networkId);
        if (goToDestroy != null){
            goToDestroy.scheduleDeath();
            _registry.remove(_header.networkId);
        }
    }
}
