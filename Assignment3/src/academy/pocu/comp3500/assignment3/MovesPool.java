package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;

import java.util.Stack;

public class MovesPool {

    public static MovesPool movesPool;
    public Stack<Move> stack = new Stack<>();

    public static MovesPool getInstance() {
        if (movesPool == null) {
            movesPool = new MovesPool();
        }

        return movesPool;
    }


    public Move alloc(final int fromX, final int fromY, final int toX, final int toY) {
        if (stack.size() == 0) {
            return new Move(fromX, fromY, toX, toY);
        }

        Move move = stack.pop();
        move.fromX = fromX;
        move.fromY = fromY;
        move.toX = toX;
        move.toY = toY;

        return move;
    }

    public void delete(Move move) {
        stack.add(move);
    }
}
