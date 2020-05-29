package Host;

import com.example.Client.ImageType;
import com.example.Client.Player;

import Common.GameObject;
import Common.PlayerHost;
import Common.SneakCommon;
import Common.Util;

public class SneakHost extends SneakCommon {
    public SneakHost(int index) {
        super(index);
    }

    @Override
    public void cast(GameObject caster){

        caster.setLook(ImageType.INVISIBLE);
        caster.getMatch().getWorldSetterHost() //기존에 존재하는 object 수정시
                .generateUpdateInstruction(caster.getNetworkId(), PlayerHost.skillDirtyFlag | PlayerHost.imageTypeDirtyFlag);
        CoreHost.getInstance().getMatch().setTimer(
                () -> caster.setLook(ImageType.MARKER),10);
        CoreHost.getInstance().getMatch().setTimer(
                () -> caster.getMatch().getWorldSetterHost() //기존에 존재하는 object 수정시
                .generateUpdateInstruction(caster.getNetworkId(), PlayerHost.imageTypeDirtyFlag),10);
    }
}
