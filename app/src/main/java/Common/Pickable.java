package Common;

public interface Pickable {
    boolean isPickedUp();
    boolean pickUp(GameObject owner);
}
