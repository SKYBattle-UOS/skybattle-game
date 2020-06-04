package Common;

import android.util.Log;

import com.example.Client.Core;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import Host.CoreHost;

public class DuplicationTrickCommon extends InstantSkill  {

    @Override
    public String getName() {
        return "분신술";
    }

    @Override
    public void cast(GameObject caster) {
        Player player = (Player) caster;
        Player anotherplayer = Core.get().getMatch().getThisPlayer();
        ReadOnlyList<GameObject> gameObjects= Core.get().getMatch().getWorld();
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject spawned = gameObjects.get(i);
                if(spawned.getName().equals(caster.getName()+"(가짜)")) {
                    if (player.getProperty().getTeam() != anotherplayer.getProperty().getTeam()) {  //다른 팀이면
                        spawned.setName(caster.getName());
                    }
                    spawned.setLook(ImageType.MARKER);
                }
            }

    }
}
