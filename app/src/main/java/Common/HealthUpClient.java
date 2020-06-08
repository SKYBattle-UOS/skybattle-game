package Common;

import com.example.Client.Core;

public class HealthUpClient extends PlayerTargetSkillClient {
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
            runCoolTime(3);
        }
    }
}
