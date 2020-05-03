package com.example.myapplication;

public class WorldSetter {
    private GameObjectRegistry _registry;
    private WorldSetterHeader _header;
    private byte[] _buffer;

    public WorldSetter(GameObjectRegistry registry){
        _registry = registry;
        _header = new WorldSetterHeader();
        _buffer = new byte[1];
    }

    public void processInstructions(InputBitStream stream){
        stream.readBytes(_buffer, 8);
        if (_buffer[0] != 'r')
            return;

        _header.setMembers(stream);

        switch (_header.action){
            case CREATE:
                createGO();
                break;
            case UPDATE:
                updateGO();
                break;
            case DESTROY:
                destroyGO();
                break;
            default:
                // do nothing
                break;
        }
    }

    private void createGO(){
        if (_registry.getGameObject(_header.networkId) == null){
            GameObject newGO = Core.getInstance().getGameObjectFactory().createGameObject(_header.classId);
            _registry.add(_header.networkId, newGO);
        }
    }

    private void updateGO(){

    }

    private void destroyGO(){
        GameObject goToDestroy = _registry.getGameObject(_header.networkId);
        if (goToDestroy != null){
            goToDestroy.scheduleDeath();
        }
    }
}
