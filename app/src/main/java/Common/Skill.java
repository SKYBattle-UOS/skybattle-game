package Common;

import com.example.Client.Core;

public abstract class Skill {
    private boolean _isDirty;
    private int _coolTime;

    public abstract String getName();
    public abstract void cast(GameObject caster);

    public final void writeToStream(OutputBitStream stream){
        if (_isDirty){
            stream.write(1, 1);
            writeToStream2(stream);
            _isDirty = false;
        }
        else
            stream.write(0, 1);

    }

    protected abstract void writeToStream2(OutputBitStream stream);

    public final void readFromStream(InputBitStream stream){
        if (stream.read(1) == 1){
            _isDirty = true;
            readFromStream2(stream);
        }
    }

    protected abstract void readFromStream2(InputBitStream stream);

    public void setDirty(boolean dirty){
        _isDirty = dirty;
    }

    public boolean isDirty(){
        return _isDirty;
    }

    protected void runCoolTime(int seconds) {
        _coolTime = seconds;
        _runCoolTime();
    }

    private void _runCoolTime(){
        int buttonIndex = Core.get().getUIManager().findButtonIndex(this);
        if (_coolTime > 0){
            Core.get().getUIManager().setButtonText(buttonIndex, String.format("%s (%d)", getName(), _coolTime));
            Core.get().getMatch().setTimer(this::_runCoolTime, 1);
            _coolTime--;
        }
        else {
            Core.get().getUIManager().setButtonActive(buttonIndex, true);
            Core.get().getUIManager().setButtonText(buttonIndex, getName());
        }
    }
}
