package Host;

import android.util.Log;

import Common.GameObject;
import Common.PlayerTargetSkill;

public class HealthUpCommon extends PlayerTargetSkill {
    @Override
    public String getName() {
        return "회복";
    }

    @Override
    public void cast(GameObject caster) {
        Log.i("hehe", "helth up networkId " + _networkId);
    }
}
