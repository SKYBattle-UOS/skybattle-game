package Common;

public class TimerStruct implements Comparable<TimerStruct> {
    public Object owner;
    public Runnable callback;
    public long timeToBeFired;

    public TimerStruct(Object owner, Runnable callback, long timeToBeFired){
        this.owner = owner;
        this.callback = callback;
        this.timeToBeFired = timeToBeFired;
    }

    @Override
    public int compareTo(TimerStruct o) {
        return Long.compare(timeToBeFired, o.timeToBeFired);
    }
}
