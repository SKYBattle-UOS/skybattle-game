package Common;

public interface Time {
    long getStartOfFrame();
    void setStartOfFrame();
    int getFrameInterval();
    int getElapsedSinceStart();
}
