package Host;


import com.example.Client.Core;
import com.example.Client.GameObjectRegistry;

import java.io.IOException;
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
        for (Map.Entry<Integer, WorldSetterHeader> entry : _mappingN2I.entrySet()){
            WorldSetterHeader header = entry.getValue();
            if (header.dirtyFlag != 0){
                CoreHost.getInstance().getNetworkManager().shouldSendThisFrame();
                try {
                    packetToSend.write(1, 8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                header.writeToStream(packetToSend);
                _registry.getGameObject(header.networkId).writeToStream(packetToSend, header.dirtyFlag);

                header.dirtyFlag = 0;
            }
            else {
                try {
                    packetToSend.write(0, 8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            entry.getValue().action = WorldSetterAction.UPDATE;
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

        header.dirtyFlag = dirtyFlag;
    }
}