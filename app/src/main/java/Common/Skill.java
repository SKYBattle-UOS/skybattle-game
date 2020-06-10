package Common;

public abstract class Skill {
    private boolean _isDirty;
    private int _coolTime;
    private UIManager _uiManager;
    private MatchCommon _match;

    public Skill(MatchCommon match, UIManager uiManager){
        _match = match;
        _uiManager = uiManager;
    }

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

    public MatchCommon getMatch(){
        return _match;
    }

    public UIManager getUIManager() { return _uiManager; }

    protected void runCoolTime(int seconds) {
        _coolTime = seconds;
        _runCoolTime();
    }

    private void _runCoolTime(){
        int buttonIndex = _uiManager.findButtonIndex(this);
        if (_coolTime > 0){
            _uiManager.setButtonText(buttonIndex, String.format("%s (%d)", getName(), _coolTime));
            _match.setTimer(this, this::_runCoolTime, 1);
            _coolTime--;
        }
        else {
            _uiManager.setButtonActive(buttonIndex, true);
            _uiManager.setButtonText(buttonIndex, getName());
        }
    }
}
