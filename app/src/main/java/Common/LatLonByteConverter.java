package Common;

public class LatLonByteConverter {
    private double _latOffset;
    private double _lonOffset;

    public void setOffset(double lat, double lon){
        _latOffset = lat;
        _lonOffset = lon;
    }

    public void restoreLatLon(int lat, int lon, double[] results){
        results[0] = ((double) lat) / 100000 + _latOffset;
        results[1] = ((double) lon) / 100000 + _lonOffset;
    }

    public void convertLatLon(double lat, double lon, int[] results){
        results[0] = (int) (lat - _latOffset) * 100000;
        results[1] = (int) (lon - _lonOffset) * 100000;
    }
}
