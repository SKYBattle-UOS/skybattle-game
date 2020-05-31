package Common;

public interface Player {
    class Friend {
        private Friend(){}
    }

    Friend friend = new Friend();

    GameObject getGameObject();
    PlayerProperty getProperty();
}
