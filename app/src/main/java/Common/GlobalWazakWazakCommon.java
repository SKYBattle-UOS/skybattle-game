package Common;

import android.util.Log;

public class GlobalWazakWazakCommon extends CoordinateSkill {
    public GlobalWazakWazakCommon(int index) {
        super(index);
    }

    @Override
    public String getName() {
        return "원격 와작와작 뻥!";
    }

    @Override
    public void cast(GameObject caster) {
        Log.i("hehe", String.format("원격 와작와작 지뢰: %f %f", _lat, _lon));
    }
}
