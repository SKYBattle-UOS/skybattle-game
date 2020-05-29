package Host;

import Common.GameObject;

public class PickUpNever implements PickUpCondition {
    @Override
    public boolean evalulate(GameObject picker) {
        return false;
    }
}
