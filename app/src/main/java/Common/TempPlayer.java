package Common;

import android.util.Log;

import com.example.Client.Core;
import com.example.Client.ImageType;

import java.io.IOException;
import java.util.Queue;

import Host.ClientProxy;
import Host.CoreHost;

/**
 * 임시 캐릭터 클래스. 볼품없는 스킬을 넣을 예정.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class TempPlayer extends GameObject {
    private int _playerId;
    public boolean isHost;

    public TempPlayer(String name) {
        super(0f, 0f, name);
        if (Core.getInstance().getRenderer() != null)
            setRenderComponent(Core.getInstance().getRenderer().createRenderComponent(this, ImageType.FILLED_CIRCLE));
        isHost = false;
    }

    public void setPlayerId(int playerId){
        _playerId = playerId;
    }

    @Override
    public void update(long ms) {
        if (isHost) {
            ClientProxy client = CoreHost.getInstance().getNetworkManager().getClientById(_playerId);
            Queue<InputState> inputs = client.getUnprocessedInputs();
            while (true){
                InputState input = inputs.poll();
                if (input == null) break;

                setPosition(input.lat, input.lon);
            }
        }
    }

    public static GameObject createInstance() {
        return new TempPlayer("TempPlayer");
    }

    @Override
    public void writeToStream(OutputBitStream stream, int dirtyFlag) {
        if ((dirtyFlag & 1) != 0) {
            double[] pos = getPosition();
            int lat = (byte) pos[0];
            int lon = (byte) pos[1];
            try {
                stream.write(lat, 8);
                stream.write(lon, 8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readFromStream(InputBitStream stream, int dirtyFlag) {
        if ((dirtyFlag & 1) != 0){
            int lat = stream.read(8);
            int lon = stream.read(8);
            setPosition(lat, lon);
        }
    }

    @Override
    public void faceDeath(){
        Log.i("Stub", "TempPlayer: " + getName() + " is dying");
    }
}
