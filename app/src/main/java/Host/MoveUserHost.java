package Host;
import Common.GameObject;
import Common.ImageType;
import Common.PlayerHost;
import Common.PlayerProperty;
import Common.Util;

public class MoveUserHost  extends MoveUserCommon {

    public void cast(GameObject caster) {

        GameObject spawned = CoreHost.get().getMatch().createGameObject(Util.ItemClassId, true);
        spawned.setName(caster.getName()+" 강제 이동되어 아이템 획득시까지 비활성화 상태입니다.");
        spawned.setPosition(_lat, _lon);
        spawned.setLook(ImageType.MARKER);

        CoreHost.get().getMatch().getWorldSetterHost().generateUpdateInstruction(caster.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }

    public void todo(){
        PlayerHost player = (PlayerHost) CoreHost.get()
                .getMatch().getRegistry().getGameObject(_networkId);

        player.

        CoreHost.get()
                .getMatch()
                .getWorldSetterHost()
                .generateUpdateInstruction(player.getNetworkId(), PlayerProperty.healthDirtyFlag);

        CoreHost.get().getMatch().getWorldSetterHost()
                .generateUpdateInstruction(player.getNetworkId(), PlayerProperty.skillDirtyFlag);
    }
}
