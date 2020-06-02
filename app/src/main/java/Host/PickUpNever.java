package Host;

import Common.GameObject;
import Common.ItemHost;

public class PickUpNever implements PickUpCondition {
    @Override
    public boolean evalulate(GameObject picker, ItemHost item) {
        return false;
    }
}
