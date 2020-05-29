package Host;

import com.example.Client.Core;

import Common.GameObject;
import Common.PlayerTargetSkill;

public class HealthUpCommon extends PlayerTargetSkill {
    public HealthUpCommon(int index) {
        super(index);
    }

    @Override
    public String getName() {
        return "회복";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == Core.getInstance().getMatch().getThisPlayer()){
            String targetName = Core.getInstance()
                    .getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.getInstance().getUIManager().setTopText(targetName + "(을)를 회복했습니다", 2);
//            Core.getInstance().getUIManager().setButtonActive(_indexInArray, false);
        }
    }
}
