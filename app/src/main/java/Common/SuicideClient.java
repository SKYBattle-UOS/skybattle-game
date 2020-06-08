package Common;

import com.example.Client.Core;

public class SuicideClient extends InstantSkillClient {
    @Override
    public String getName() {
        return "자살";
    }

    @Override
    public void cast(GameObject caster) {
        if (caster == Core.get().getMatch().getThisPlayer()){
            Core.get().getUIManager().setTopText("체력을 감소했습니다", 3);
            runCoolTime(3);
        }
    }
}
