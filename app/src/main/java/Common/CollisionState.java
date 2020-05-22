package Common;

public class CollisionState {
    public GameObject other;
    public boolean isEnter;
    public boolean isExit;

    @Override
    public boolean equals(Object object){
        if (object instanceof GameObject)
            return other == object;

        return super.equals(object);
    }
}
