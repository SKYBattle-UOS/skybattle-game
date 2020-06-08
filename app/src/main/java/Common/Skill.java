package Common;

public abstract class Skill {
    private boolean _isDirty;

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
}
