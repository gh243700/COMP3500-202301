package academy.pocu.comp3500.lab2;

import academy.pocu.comp3500.lab2.datastructure.Node;

public final class Queue {
    Node front;
    Node back;

    int size;

    public void enqueue(final int data) {
        ++size;
        if (back == null) {
            back = LinkedList.append(null, data);
            front = back;
            return;
        }
        //front -> back
        back = LinkedList.append(back, data);
        back = back.getNextOrNull();
    }

    public int peek() {
        return front.getData();
    }

    public int dequeue() {
        int data = front.getData();
        front = LinkedList.removeAt(front, 0);
        --size;
        return data;
    }

    public int getSize() {
        return size;
    }
}