package Common;

import com.example.Client.Core;


public class SneakClient extends InstantSkillClient {

    @Override
    public String getName() {
        return "은신";
    }

    @Override
    public void cast(GameObject caster) {
        Player player = (Player)caster;
        Player anotherplayer = Core.get().getMatch().getThisPlayer();
        if (player.getProperty().getTeam() != anotherplayer.getProperty().getTeam()) { //다른  팀이면
            caster.setLook(ImageType.INVISIBLE);
            Core.get().getMatch().setTimer(
                    () -> caster.setLook(ImageType.MARKER),60); //60초뒤 다시 marker

        }
    }
}

