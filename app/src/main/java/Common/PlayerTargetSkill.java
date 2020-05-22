package Common;

import java.io.IOException;

public abstract class PlayerTargetSkill extends Skill {
    protected int _networkId;

    public PlayerTargetSkill(int index) {
        super(index);
    }

    public void setTargetPlayer(int networkId) {
        _networkId = networkId;
    }

    @Override
    protected void writeToStream2(OutputBitStream stream) throws IOException {
        stream.write(_networkId, 32);
    }

    @Override
    protected void readFromStream2(InputBitStream stream) {
        _networkId = stream.read(32);
    }
}
