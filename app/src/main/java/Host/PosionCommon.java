package Host;

import com.example.Client.Core;

import Common.GameObject;
import Common.PlayerTargetSkill;

public class PosionCommon extends PlayerTargetSkill {
    public PosionCommon(int index){super(index);}

    public String getName(){
        return "독공격";
    }

    public void cast(GameObject caster){
        if (caster == Core.getInstance().getInputManager().getThisPlayer()) {
            String targetName = Core.getInstance().getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.getInstance().getUIManager().setTopText(targetName + "(을)를 독 공격 했습니다.");
            Core.getInstance().getUIManager().setButtonActive(indexInArray, false);
        }
    }
}
