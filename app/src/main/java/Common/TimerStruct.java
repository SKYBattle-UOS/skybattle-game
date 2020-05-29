package Common;

public class TimerStruct implements Comparable<TimerStruct> {
    public Runnable callback;
    public long timeToBeFired;

    public TimerStruct(Runnable callback, long timeToBeFired){
        this.callback = callback;
        this.timeToBeFired = timeToBeFired;
    }

    @Override
    public int compareTo(TimerStruct o) {
        return Long.compare(timeToBeFired, o.timeToBeFired);
    }
}
