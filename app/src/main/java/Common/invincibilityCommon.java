package Common;

import com.example.Client.Core;

public class invincibilityCommon extends PlayerTargetSkill {
    public invincibilityCommon(int index) {
        super(index);
    }

    @Override
    public String getName() {
        return "무적 !!";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == Core.getInstance().getInputManager().getThisPlayer()){
            Core.getInstance().getUIManager().setTopText("무적을 사용 했습니다", 10);
//            Core.getInstance().getUIManager().setButtonActive(_indexInArray, false);
        }
    }
}
