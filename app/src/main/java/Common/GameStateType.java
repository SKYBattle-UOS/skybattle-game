package Common;

import com.example.Client.GameStateContext;
import com.example.Client.GameStateMatch;
import com.example.Client.GameStateRoom;

/**
 * GameStateContext 에서 사용하는 Enum.
 * @see GameStateContext
 * @see MatchStateType
 */
public enum GameStateType {
    /**
     * 방 화면
     * @see GameStateRoom
     */
    ROOM,
    /**
     * 매치 화면
     * @see GameStateMatch
     */
    MATCH
}