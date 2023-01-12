package academy.pocu.comp3500.lab2.datastructure;

import java.util.Objects;

public final class Node {
    private final int data;
    private Node next;

    public Node(final int data) {
        this.data = data;
    }

    public int getData() {
        return this.data;
    }

    public Node getNextOrNull() {
        return this.next;
    }

    public void setNext(final Node node) {
        this.next = node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return data == node.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, next);
    }
}
