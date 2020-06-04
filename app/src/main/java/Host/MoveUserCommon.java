package Host;

import com.example.Client.Core;

import Common.CoordinateSkill;
import Common.GameObject;
public class MoveUserCommon extends CoordinateSkill {

    @Override
    public String getName() {
        return "상대 강제이동!";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == Core.get().getMatch().getThisPlayer().getGameObject()){
            String targetName = Core.get()
                    .getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "는 아이템을 획득하여야 죽음 상태가 부활됩니다.", 2);

            int btnIndex = Core.get().getUIManager().findButtonIndex(this);
            Core.get().getUIManager().setButtonActive(btnIndex, false);
        }

    }
}
