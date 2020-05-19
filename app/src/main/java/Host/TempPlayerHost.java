package Host;

import java.util.Queue;

import Common.GameObject;
import Common.InputState;
import Common.Player;

public class TempPlayerHost extends Player {
    public TempPlayerHost(float latitude, float longitude, String name){
        super(latitude, longitude, name);
    }

    public static GameObject createInstance(){
        return new TempPlayerHost(0, 0, "TempPlayer");
    }

    @Override
    public void update(long ms) {
        int dirtyFlag = 0;

        ClientProxy client = CoreHost.getInstance().getNetworkManager().getClientById(getPlayerId());
        Queue<InputState> inputs = client.getUnprocessedInputs();
        while (true) {
            InputState input = inputs.poll();
            if (input == null) break;

            double[] prevPos = getPosition();
            if (prevPos[0] != input.lat || prevPos[1] != input.lon) {
                setPosition(input.lat, input.lon);
                dirtyFlag |= 1;
            }
        }

        _worldSetterHost.generateUpdateInstruction(getNetworkId(), dirtyFlag);
    }
}
