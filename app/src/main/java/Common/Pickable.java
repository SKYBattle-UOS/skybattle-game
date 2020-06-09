package Common;

public interface Pickable {
    boolean isPickedUp();
    boolean getPickedUpBy(GameObject owner);
}
