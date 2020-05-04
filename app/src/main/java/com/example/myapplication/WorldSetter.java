package com.example.myapplication;

import java.util.Vector;

public class WorldSetter {
    private GameObjectRegistry _registry;
    private WorldSetterHeader _header;
    private byte[] _buffer;
    private Vector<GameObject> _world;

    public WorldSetter(Vector<GameObject> world, GameObjectRegistry registry){
        _registry = registry;
        _header = new WorldSetterHeader();
        _buffer = new byte[1];
        _world = world;
    }

    public void processInstructions(InputBitStream stream){
        // TODO: check remaining bits in the stream
        while (true){
            stream.readBytes(_buffer, 8);
            if (_buffer[0] != 'r')
                return;

            _header.setMembers(stream);

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
            newGO.readFromStream(stream);
            _registry.add(_header.networkId, newGO);
            newGO.setIndexInWorld(_world.size());
            _world.add(newGO);
        }
    }

    private void updateGO(InputBitStream stream){
        GameObject goToUpdate = _registry.getGameObject(_header.networkId);
        if (goToUpdate == null) return;

        goToUpdate.readFromStream(stream);
    }

    private void destroyGO(){
        GameObject goToDestroy = _registry.getGameObject(_header.networkId);
        if (goToDestroy != null){
            goToDestroy.scheduleDeath();
            _registry.remove(_header.networkId);
        }
    }
}
