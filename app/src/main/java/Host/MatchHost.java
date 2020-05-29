package Host;

import Common.GameObject;
import Common.MatchCommon;

public interface MatchHost extends MatchCommon {
    WorldSetterHost getWorldSetterHost();
    GameObject createGameObject(int classId, boolean addToCollider);
}
