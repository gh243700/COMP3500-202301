package academy.pocu.comp3500.lab6;

public class NodeWrapper {
    private Node parent;
    private Node child;

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public Node getParent() {
        return parent;
    }

    public boolean isChildAtRightSideOfParent() {
        if (parent.getLeft() == child) {
            return false;
        }

        return true;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
