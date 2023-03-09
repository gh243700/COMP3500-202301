package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;

import java.util.Stack;

public class MovesPool {
    public Stack<Move> pool = new Stack<>();
    private static MovesPool instance;
    public static MovesPool getInstance() {
        if (instance == null) {
            instance = new MovesPool();
        }
        return instance;
    }

    private MovesPool() {
        pool.ensureCapacity(128);
        for (int i = 0; i < pool.capacity(); ++i) {
            pool.add(new Move());
        }
    }

    public Move alloc(final int fromX, final int fromY, final int toX, final int toY) {

        if (pool.size() == 0) {
            return new Move(fromX, fromY, toX, toY);
        }

        Move result = pool.pop();
        result.fromX = fromX;
        result.fromY = fromY;
        result.toX = toX;
        result.toY = toY;

        return result;
    }

    public void delete(Move move) {
        pool.add(move);
    }
}
