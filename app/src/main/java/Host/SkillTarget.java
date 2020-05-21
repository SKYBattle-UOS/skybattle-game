package Host;

public class SkillTarget {
    public int qwer;
    public double lat;
    public double lon;
    public int playerId = -1;

    public SkillTarget(int qwer, double lat, double lon){
        this.qwer = qwer;
        this.lat = lat;
        this.lon = lon;
    }

    public SkillTarget(int qwer, int playerId){
        this.qwer = qwer;
        this.playerId = playerId;
    }

    public SkillTarget(int qwer){
        this.qwer = qwer;
    }
}
