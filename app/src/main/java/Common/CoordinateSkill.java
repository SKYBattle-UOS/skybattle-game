package Common;

import com.example.Client.Core;

import java.io.IOException;

import Host.CoreHost;

public abstract class CoordinateSkill extends Skill {
    protected double _lat;
    protected double _lon;

    private int[] _intTemp = new int[2];
    private double[] _doubleTemp = new double[2];

    public CoordinateSkill(int index) {
        super(index);
    }

    public void setTargetCoord(double lat, double lon) {
        _lat = lat;
        _lon = lon;
    }

    @Override
    protected void writeToStream2(OutputBitStream stream) throws IOException {
        CoreHost.getInstance().getMatch().getConverter().convertLatLon(_lat, _lon, _intTemp);
        stream.write(_intTemp[0], 32);
        stream.write(_intTemp[1], 32);
    }

    @Override
    protected void readFromStream2(InputBitStream stream) {
        int lat = stream.read(32);
        int lon = stream.read(32);
        Core.getInstance().getMatch().getConverter().restoreLatLon(lat, lon, _doubleTemp);
        _lat = _doubleTemp[0];
        _lon = _doubleTemp[1];
    }
}
