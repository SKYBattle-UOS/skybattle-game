package Host;
import com.example.Client.Core;
import Common.GameObject;
import Common.ImageType;
import Common.Player;
import Common.PlayerTargetSkill;

public class CantAttackCommon extends PlayerTargetSkill {
    public String getName(){
        return "공격 무력화";
    }

    public void cast(GameObject caster){
        if (caster == Core.get().getMatch().getThisPlayer()) {
            String targetName = Core.get().getMatch().getRegistry().getGameObject(_networkId).getName();
            Core.get().getUIManager().setTopText(targetName + "(을)를 공력을 10초 동안 무력화 했습니다.");
            int btnIndex = Core.get().getUIManager().findButtonIndex(this);
            Core.get().getUIManager().setButtonActive(btnIndex, false);
        }

/*
        Player player = (Player)caster;
        Player anotherplayer=  Core.get().getMatch().getThisPlayer();
        if (player.getProperty().getTeam() != anotherplayer.getProperty().getTeam()) { //다른  팀이면
            caster.setLook(ImageType.INVISIBLE);
            Core.get().getMatch().setTimer(
                    () -> caster.setLook(ImageType.MARKER),60); //60초뒤 다시 marker

        }*/
    }
}
