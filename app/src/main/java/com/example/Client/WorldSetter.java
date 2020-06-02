package com.example.Client;

import android.util.Log;

import java.util.List;

import Common.GameObject;
import Common.InputBitStream;
import Common.Player;
import Common.WorldSetterHeader;

public class WorldSetter {
    private GameStateMatch _match;
    private WorldSetterHeader _header = new WorldSetterHeader();
    private List<GameObject> _world;
    private List<Player> _players;


    public WorldSetter(GameStateMatch match, List<GameObject> world, List<Player> players){
        _match = match;
        _world = world;
        _players = players;
    }

    public void processInstructions(InputBitStream stream){
        while (true){
            if (stream.read(1) != 1) return;

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
        if (_match.getRegistry().getGameObject(_header.networkId) == null){
            GameObject newGO = Core.get().getGameObjectFactory().createGameObject(_header.classId);

            if (newGO == null){
                Log.i("hehe", "what's goign on" + _header.classId);
            }

            newGO.setMatch(_match);
            newGO.setNetworkId(_header.networkId);
            newGO.readFromStream(stream, _header.dirtyFlag);
            _match.getRegistry().add(_header.networkId, newGO);
            newGO.setIndexInWorld(_match.getWorld().size());
            _world.add(newGO);

            if (newGO instanceof Player)
                _players.add((Player) newGO);
        }
    }

    private void updateGO(InputBitStream stream){
        GameObject goToUpdate = _match.getRegistry().getGameObject(_header.networkId);
        if (goToUpdate == null) return;

        goToUpdate.readFromStream(stream, _header.dirtyFlag);
    }

    private void destroyGO(){
        GameObject goToDestroy = _match.getRegistry().getGameObject(_header.networkId);
        if (goToDestroy != null){
            goToDestroy.scheduleDeath();
            _match.getRegistry().remove(_header.networkId);

            if (goToDestroy instanceof PlayerClient)
                _players.remove(goToDestroy);
        }
    }
}
