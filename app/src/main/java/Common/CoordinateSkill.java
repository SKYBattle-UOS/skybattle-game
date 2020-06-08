package Common;

abstract class CoordinateSkill extends Skill {
    protected double _lat;
    protected double _lon;

    private int[] _intTemp = new int[2];
    private double[] _doubleTemp = new double[2];

    public CoordinateSkill(MatchCommon match) {
        super(match);
    }

    public void setTargetCoord(double lat, double lon) {
        _lat = lat;
        _lon = lon;
    }

    @Override
    protected void writeToStream2(OutputBitStream stream) {
        getMatch().getConverter().convertLatLon(_lat, _lon, _intTemp);
        stream.write(_intTemp[0], 32);
        stream.write(_intTemp[1], 32);
    }

    @Override
    protected void readFromStream2(InputBitStream stream) {
        int lat = stream.read(32);
        int lon = stream.read(32);
        getMatch().getConverter().restoreLatLon(lat, lon, _doubleTemp);
        _lat = _doubleTemp[0];
        _lon = _doubleTemp[1];
    }
}
