package Common;

import com.example.Client.Core;
import com.example.Client.ImageType;
import com.example.Client.RenderComponent;

import Host.CoreHost;

public class SneakCommon extends InstantSkill {
    public SneakCommon(int index) {
        super(index);
    }

    @Override
    public String getName() {
        return "은신";
    }

    @Override
    public void cast(GameObject caster) {
        PlayerCommon player = (PlayerCommon)caster;
        PlayerCommon anotherplayer=Core.get().getMatch().getThisPlayer();
        if (player.getTeam() == anotherplayer.getTeam()) { //다른 팀 팀이면
            anotherplayer.setRenderComponent(Core.get().getRenderer().createRenderComponent(caster, ImageType.INVISIBLE));//인비저블로 바꿔주고
            Core.get().getMatch().setTimer(
                    () -> anotherplayer.setRenderComponent(Core.get().getRenderer().createRenderComponent(caster, ImageType.MARKER)),10); //10초뒤 다시 marker

        }
    }
}

