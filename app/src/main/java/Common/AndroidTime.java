package Common;

import android.os.SystemClock;

public class AndroidTime implements Time {
    private long _startOfFrame;
    private long _lastStartOfFrame;

    @Override
    public long getStartOfFrame(){
        return _startOfFrame;
    }

    @Override
    public void setStartOfFrame(){
        _lastStartOfFrame = _startOfFrame;
        _startOfFrame = SystemClock.uptimeMillis();
    }

    @Override
    public int getFrameInterval(){
        return (int) (_startOfFrame - _lastStartOfFrame);
    }

    @Override
    public int getElapsedSinceStart(){
        return (int) (SystemClock.uptimeMillis() - _startOfFrame);
    }
}
