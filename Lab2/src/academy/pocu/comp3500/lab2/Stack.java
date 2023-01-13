package academy.pocu.comp3500.lab2;

import academy.pocu.comp3500.lab2.datastructure.Node;

public final class Stack {

    Node mStack;
    int mSize;

    public void push(final int data) {
        ++mSize;
        if (mStack == null) {
            mStack = LinkedList.append(null, data);
            return;
        }

        mStack = LinkedList.prepend(mStack, data);
    }

    public int peek() {
        return LinkedList.getOrNull(mStack, 0).getData();
    }

    public int pop() {

        int data = peek();
        mStack = LinkedList.removeAt(mStack, 0);
        --mSize;
        return data;
    }

    public int getSize() {
        return mSize;
    }
}