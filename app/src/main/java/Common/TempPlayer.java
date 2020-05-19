package Common;

import android.util.Log;

import com.example.Client.Core;
import com.example.Client.ImageType;

import java.io.IOException;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;
import Host.WorldSetterHost;

/**
 * 임시 캐릭터 클래스. 볼품없는 스킬을 넣을 예정.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class TempPlayer extends Player {
    public TempPlayer(String name) {
        super(0f, 0f, name);
        setRenderComponent(Core.getInstance().getRenderer().createRenderComponent(this, ImageType.FILLED_CIRCLE));
    }

    @Override
    public void update(long ms) {}

    public static GameObject createInstance() {
        return new TempPlayer("TempPlayer");
    }

    @Override
    public void faceDeath(){
        Log.i("Stub", "TempPlayer: " + getName() + " is dying");
    }
}
