package academy.pocu.comp3500.lab2;

import academy.pocu.comp3500.lab2.datastructure.Node;

public final class Stack {

    Node stack;
    int size;

    public void push(final int data) {
        ++size;
        if (stack == null) {
            stack = LinkedList.append(null, data);
            return;
        }

        stack = LinkedList.prepend(stack, data);
    }

    public int peek() {
        return LinkedList.getOrNull(stack, 0).getData();
    }

    public int pop() {

        int data = peek();
        stack = LinkedList.removeAt(stack, 0);
        --size;
        return data;
    }

    public int getSize() {
        return size;
    }
}