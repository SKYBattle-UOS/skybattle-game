package Common;

import com.example.Client.Core;
import com.example.Client.PlayerClient;


public class SneakCommon extends InstantSkill {

    @Override
    public String getName() {
        return "은신";
    }

    @Override
    public void cast(GameObject caster) {
        Player player = (Player)caster;
        Player anotherplayer=  Core.get().getMatch().getThisPlayer();
        if (player.getProperty().getTeam() != anotherplayer.getProperty().getTeam()) { //다른  팀이면
            caster.setLook(ImageType.INVISIBLE);
            Core.get().getMatch().setTimer(
                    () -> caster.setLook(ImageType.MARKER),10); //10초뒤 다시 marker

        }
    }
}

