package Common;

//import com.example.Client.Core;

public class InvincibilityCommon extends PlayerTargetSkill {
    public InvincibilityCommon(MatchCommon match, UIManager uiManager) {
        super(match, uiManager);
    }

    @Override
    public String getName() {
        return "무적 !!";
    }

    @Override
    public void cast(GameObject caster) {
//        if (caster == Core.get().getMatch().getThisPlayer()){
//            Core.get().getUIManager().setTopText("무적을 사용 했습니다", 10);
//            int btnIndex = Core.get().getUIManager().findButtonIndex(this);
//            Core.get().getUIManager().setButtonActive(btnIndex, false);
//        }
    }
}
