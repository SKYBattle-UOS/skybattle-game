package Common;

import com.example.Client.Core;

import Common.GameObject;
import Common.PlayerTargetSkill;

public class HealthUpCommon extends PlayerTargetSkill {
    @Override
    public String getName() {
        return "회복";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == Core.get().getMatch().getThisPlayer().getGameObject()){
            String targetName = Core.get()
                    .getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "(을)를 회복했습니다", 2);

            int btnIndex = Core.get().getUIManager().findButtonIndex(this);
            Core.get().getUIManager().setButtonActive(btnIndex, false);
        }
    }
}
