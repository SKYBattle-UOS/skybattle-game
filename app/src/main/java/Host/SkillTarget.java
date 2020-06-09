package Host;

public class SkillTarget {
    public double lat;
    public double lon;
    public int networkId = -1;

    public SkillTarget(){
    }

    public SkillTarget(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public SkillTarget(int networkId){
        this.networkId = networkId;
    }
}
