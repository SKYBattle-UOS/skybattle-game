package Common;

public class InputState {
    private double _lat;
    private double _lon;
    private boolean _q, _w, _e, _r;

    public double[] getPosition(){
        return new double[]{_lat, _lon};
    }

    public boolean isQ(){
        return _q;
    }

    public boolean isW(){
        return _w;
    }

    public boolean isE(){
        return _e;
    }

    public boolean isR(){
        return _r;
    }

    public void writeToStream(OutputBitStream stream){
        // TODO
    }

    public void readFromStream(InputBitStream stream){
        // TODO
    }
}
