package academy.pocu.comp3500.lab2;

import academy.pocu.comp3500.lab2.datastructure.Node;

public final class Queue {
    Node mFront;
    Node mBack;

    int mSize;

    public void enqueue(final int data) {
        ++mSize;
        if (mBack == null) {
            mBack = LinkedList.append(null, data);
            mFront = mBack;
            return;
        }
        //front -> back
        mBack = LinkedList.append(mBack, data);
        mBack = mBack.getNextOrNull();
    }

    public int peek() {
        return mFront.getData();
    }

    public int dequeue() {
        int data = mFront.getData();
        mFront = LinkedList.removeAt(mFront, 0);
        --mSize;
        return data;
    }

    public int getSize() {
        return mSize;
    }
}