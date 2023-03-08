package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;

import java.util.Stack;

public class WrappersPool {
    public Stack<Wrapper> pool = new Stack<>();
    private static WrappersPool instance;
    public static WrappersPool getInstance() {
        if (instance == null) {
            instance = new WrappersPool();
        }
        return instance;
    }

    private WrappersPool(){
        pool.ensureCapacity(128);
        for (int i = 0; i < pool.capacity(); ++i) {
            pool.add(new Wrapper());
        }
    }

    public Wrapper alloc(int eval, Move move) {
        if (pool.size() == 0) {
            return new Wrapper(eval, move);
        }

        Wrapper wrapper = pool.pop();
        wrapper.reset(eval, move);

        return wrapper;
    }

    public void delete(Wrapper node) {
        pool.add(node);
    }
}
