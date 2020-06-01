package Host;

import Common.GameObject;
import Common.ItemHost;

public interface PickUpCondition {
    boolean evalulate(GameObject picker, ItemHost item);
}
