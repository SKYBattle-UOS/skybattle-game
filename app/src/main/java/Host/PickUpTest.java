package Host;

import Common.GameObject;
import Common.ItemHost;

public class PickUpTest implements PickUpCondition {
    @Override
    public boolean evalulate(GameObject picker, ItemHost item) {
        return item.getName().equals("포션");
    }
}
