package Host;

import com.example.Client.Core;

import Common.GameObject;
import Common.PlayerTargetSkill;

public class PosionCommon extends PlayerTargetSkill {

    public PosionCommon(int index){setTargetPlayer(index);}

    public String getName(){
        return "독공격";
    }

    public void cast(GameObject caster){
        if (caster == Core.get().getMatch().getThisPlayer()) {
            String targetName = Core.get().getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "(을)를 독 공격 했습니다.");
           // Core.get().getUIManager().setButtonActive(indexInArray, false);
        }
    }
}