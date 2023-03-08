package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;

import java.util.Stack;

public class NodePool {
    public Stack<Wrapper> pool = new Stack<>();
    private static NodePool instance;
    public int allocCount = 0;
    public int deleteCount = 0;

    public static NodePool getInstance() {
        if (instance == null) {
            instance = new NodePool();
        }

        return instance;
    }

    private NodePool(){
        pool.ensureCapacity(128);
        for (int i = 0; i < pool.capacity(); ++i) {
            //pool.add();
        }

    }

    public Wrapper alloc(Bitmap bitmap, Move move) {
        ++allocCount;
        if (pool.size() == 0) {
            //return new Wrapper(bitmap, move);
        }

        Wrapper node = pool.pop();
        //node.reset(bitmap, move);

        return node;
    }

    public void delete(Wrapper node) {
        ++deleteCount;
        pool.add(node);
    }
}
