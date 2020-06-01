package Host;


import Common.GameObject;
import Common.PlayerHost;
import Common.PlayerProperty;
import Common.SneakCommon;
import Common.Util;

public class SneakHost extends SneakCommon {

    @Override
    public void cast(GameObject caster){
        /*
        caster.setLook(ImageType.INVISIBLE);
        CoreHost.get().getMatch().getWorldSetterHost() //기존에 존재하는 object 수정시
                .generateUpdateInstruction(caster.getNetworkId(), PlayerHost.skillDirtyFlag | PlayerHost.imageTypeDirtyFlag);
        CoreHost.get().getMatch().setTimer(
                () -> caster.setLook(ImageType.MARKER),10);
        CoreHost.get().getMatch().setTimer(
                () -> CoreHost.get().getMatch().getWorldSetterHost() //기존에 존재하는 object 수정시
                .generateUpdateInstruction(caster.getNetworkId(), PlayerHost.imageTypeDirtyFlag),10);
         */
        CoreHost.get().getMatch().getWorldSetterHost()
                .generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}
