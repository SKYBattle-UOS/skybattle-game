package Common;

abstract class PlayerTargetSkill extends Skill {
    protected int _networkId;

    public PlayerTargetSkill(MatchCommon match) {
        super(match);
    }

    public void setTargetPlayer(int networkId) {
        _networkId = networkId;
    }

    @Override
    protected void writeToStream2(OutputBitStream stream) {
        stream.write(_networkId, 32);
    }

    @Override
    protected void readFromStream2(InputBitStream stream) {
        _networkId = stream.read(32);
    }
}
