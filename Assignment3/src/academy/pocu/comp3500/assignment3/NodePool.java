package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;

import java.util.Stack;

public class NodePool {
    public Stack<Node> pool = new Stack<>();
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
            pool.add(new Node());
        }

    }

    public Node alloc(Bitmap bitmap, Move move, Node parent) {
        ++allocCount;
        if (pool.size() == 0) {
            return new Node(bitmap, move, parent);
        }

        Node node = pool.pop();
        node.reset(bitmap, move, parent);

        return node;
    }

    public void delete(Node node) {
        ++deleteCount;
        pool.add(node);
    }
}
