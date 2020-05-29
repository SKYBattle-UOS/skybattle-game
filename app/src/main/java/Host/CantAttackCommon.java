package Host;
import com.example.Client.Core;
import Common.GameObject;
import Common.PlayerTargetSkill;

public class CantAttackCommon extends PlayerTargetSkill {
    public CantAttackCommon(int index){super(index);}

    public String getName(){
        return "공격 무력화";
    }

    public void cast(GameObject caster){
        if (caster == Core.get().getMatch().getThisPlayer()) {
            String targetName = Core.get().getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "(을)를 공력을 10초 동안 무력화 했습니다.");
            Core.get().getUIManager().setButtonActive(_indexInArray, false);
        }//버튼 하드코딩
    }
}
