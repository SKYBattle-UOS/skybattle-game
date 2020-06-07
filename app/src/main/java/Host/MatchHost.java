package Host;

import Common.GameObject;
import Common.MatchCommon;

public interface MatchHost extends MatchCommon {
    GameObject createGameObject(int classId, boolean addToCollider);
}
