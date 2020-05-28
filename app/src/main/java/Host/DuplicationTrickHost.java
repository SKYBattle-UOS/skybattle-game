package Host;

import com.example.Client.ImageType;

import Common.DuplicationTrickCommon;
import Common.GameObject;
import Common.PlayerHost;
import Common.Util;

public class DuplicationTrickHost extends DuplicationTrickCommon {
    public DuplicationTrickHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster) {
        for(int i=0;i<3;i++) {
            GameObject spawned = caster.getMatch().createGameObject(Util.ItemClassId, true);
            spawned.setName(caster.getName());
            if (i == 0)
                spawned.setPosition(caster.getPosition()[0] + 0.001, caster.getPosition()[1] - 0.001);
            else if (i == 1)
                spawned.setPosition(caster.getPosition()[0] + 0.003, caster.getPosition()[1] - 0.005);
            else
                spawned.setPosition(caster.getPosition()[0] - 0.002, caster.getPosition()[1] + 0.001);
            spawned.setLook(ImageType.MARKER);
            spawned.getMatch().getWorldSetterHost().generateUpdateInstruction(caster.getNetworkId(), PlayerHost.skillDirtyFlag);
        }
    }
}
