package Common;

public interface Pickable {
    boolean isPickedUp();
    void pickUp(GameObject owner);
}
