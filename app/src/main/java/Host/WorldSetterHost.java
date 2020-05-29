package Host;

import android.util.Log;

import com.example.Client.GameObjectRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Common.OutputBitStream;
import Common.WorldSetterAction;
import Common.WorldSetterHeader;

public class WorldSetterHost {
    private Map<Integer, WorldSetterHeader> _mappingN2I;
    private GameObjectRegistry _registry;

    public WorldSetterHost(GameObjectRegistry registry){
        _mappingN2I = new HashMap<>();
        _registry = registry;
    }

    public void writeInstructionToStream(OutputBitStream packetToSend) {
        ArrayList<Integer> _toRemove = new ArrayList<>();
        for (Map.Entry<Integer, WorldSetterHeader> entry : _mappingN2I.entrySet()){
            WorldSetterHeader header = entry.getValue();
            if (header.dirtyFlag != 0){
                CoreHost.get().getNetworkManager().shouldSendThisFrame();

                try {
                    packetToSend.write(1, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                header.writeToStream(packetToSend);

                if (header.action != WorldSetterAction.DESTROY){
                    _registry.getGameObject(header.networkId).writeToStream(packetToSend, header.dirtyFlag);
                    entry.getValue().action = WorldSetterAction.UPDATE;
                }
                else
                    _toRemove.add(header.networkId);

                header.dirtyFlag = 0;
            }
        }

        for (int nid : _toRemove)
            _mappingN2I.remove(nid);

        try {
            packetToSend.write(0, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateCreateInstruction(int classId, int networkId, int dirtyFlag) {
        WorldSetterHeader header = new WorldSetterHeader();
        header.action = WorldSetterAction.CREATE;
        header.networkId = networkId;
        header.classId = classId;
        header.dirtyFlag = dirtyFlag;

        _mappingN2I.put(networkId, header);
    }

    public void generateUpdateInstruction(int networkId, int dirtyFlag){
        WorldSetterHeader header = _mappingN2I.get(networkId);
        if (header == null || header.action != WorldSetterAction.UPDATE) return;

        header.dirtyFlag |= dirtyFlag;
    }

    public void generateDestroyInstruction(int networkId){
        WorldSetterHeader header = _mappingN2I.get(networkId);
        if (header == null || header.action != WorldSetterAction.UPDATE){
            Log.i("hehe", "something wrong");
            return;
        }

        header.action = WorldSetterAction.DESTROY;
        header.networkId = networkId;
        header.dirtyFlag = -1;
    }
}