package Common;

import java.util.Deque;

public class MoveList {
    private Deque<Move> _moves;

    public void append(Move move){
        _moves.addLast(move);
    }
}
