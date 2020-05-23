package Host;

public class SkillTarget {
    public int qwer;
    public double lat;
    public double lon;
    public int networkId = -1;

    public SkillTarget(int qwer, double lat, double lon){
        this.qwer = qwer;
        this.lat = lat;
        this.lon = lon;
    }

    public SkillTarget(int qwer, int networkId){
        this.qwer = qwer;
        this.networkId = networkId;
    }

    public SkillTarget(int qwer){
        this.qwer = qwer;
    }
}
