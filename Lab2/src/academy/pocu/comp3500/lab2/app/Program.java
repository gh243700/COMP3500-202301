package academy.pocu.comp3500.lab2.app;

import academy.pocu.comp3500.lab2.LinkedList;
import academy.pocu.comp3500.lab2.Queue;
import academy.pocu.comp3500.lab2.Stack;
import academy.pocu.comp3500.lab2.datastructure.Node;

public class Program {

    public static void testStack() {
        Stack stack = new Stack();
        for (int i = 0; i < 10; ++i) {
            stack.push(i + 1);
            assert (stack.getSize() == i + 1);
        }

        for (int i = 0; i < 10; ++i) {
            assert (stack.peek() == 10 - i);
            assert (stack.getSize() == 10 - i);
            assert (stack.pop() == 10 - i);
            assert (stack.getSize() == 10 - i - 1);
        }
    }

    public static void testQueue() {
        Queue queue = new Queue();

        for (int i = 0; i < 10; ++i) {
            queue.enqueue(i + 1);
            assert (queue.getSize() == i + 1);
        }

        for (int i = 0; i < 10; ++i) {
            assert (queue.peek() == i + 1);
            assert (queue.getSize() == 10 - i);
            assert (queue.dequeue() == i + 1);
            assert (queue.getSize() == 10 - i - 1);
        }

    }

    public static void TestLinkedList() {

        {
            Node root = null;

            for (int i = 0; i < 10; ++i) {
                root = LinkedList.append(root, i + 1);
            }

            //LinkedList.testLinkedList(root, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        }
        {
            Node root = null;

            root = LinkedList.prepend(root, 10);
            //LinkedList.testLinkedList(root, new int[]{10});
            root = LinkedList.prepend(root, 9);
            root = LinkedList.prepend(root, 8);
            root = LinkedList.prepend(root, 7);
            root = LinkedList.prepend(root, 6);
            root = LinkedList.prepend(root, 5);
            root = LinkedList.prepend(root, 4);
            root = LinkedList.prepend(root, 3);
            root = LinkedList.prepend(root, 2);
            root = LinkedList.prepend(root, 1);

            //LinkedList.testLinkedList(root, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        }

        {//insertAt
            Node root = null;

            for (int i = 0; i < 10; ++i) {
                if (i % 2 == 0) {
                    root = LinkedList.append(root, i + 1);
                }
            }

            //LinkedList.testLinkedList(root, new int[]{1, 3, 5, 7, 9});

            for (int i = 0; i < 11; ++i) {
                if (i % 2 == 0) {
                    root = LinkedList.insertAt(root, i, i);
                }

            }
            //LinkedList.testLinkedList(root, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});


            root = LinkedList.insertAt(root, -1, 100);
            //LinkedList.testLinkedList(root, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});


            root = LinkedList.insertAt(root, 12, 100);
            //LinkedList.testLinkedList(root, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        }

        { //removeAt
            Node root = null;

            for (int i = 0; i < 10; ++i) {
                root = LinkedList.append(root, i + 1);
            }

            //LinkedList.testLinkedList(root, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

            root = LinkedList.removeAt(root, 0);
            //LinkedList.testLinkedList(root, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10});

            root = LinkedList.removeAt(root, 8);
            //LinkedList.testLinkedList(root, new int[]{2, 3, 4, 5, 6, 7, 8, 9});

            root = LinkedList.removeAt(root, 2);
            //LinkedList.testLinkedList(root, new int[]{2, 3, 5, 6, 7, 8, 9});

            root = LinkedList.removeAt(root, -1);
            //LinkedList.testLinkedList(root, new int[]{2, 3, 5, 6, 7, 8, 9});


            root = LinkedList.removeAt(root, 7);
            //LinkedList.testLinkedList(root, new int[]{2, 3, 5, 6, 7, 8, 9});


            root = LinkedList.removeAt(root, 6);
            //LinkedList.testLinkedList(root, new int[]{2, 3, 5, 6, 7, 8});
        }

        {//getIndexOf
            Node root = null;

            for (int i = 0; i < 10; ++i) {
                root = LinkedList.append(root, i + 1);
            }

            for (int i = 0; i < 10; ++i) {
                assert (LinkedList.getIndexOf(root, i + 1) == i);
            }

            assert (LinkedList.getIndexOf(root, 0) == -1);
            assert (LinkedList.getIndexOf(root, -1) == -1);
            assert (LinkedList.getIndexOf(root, 11) == -1);
        }

        {//getOrNull
            Node root = null;

            for (int i = 0; i < 10; ++i) {
                root = LinkedList.append(root, i + 1);
            }

            for (int i = 0; i < 10; ++i) {
                assert (LinkedList.getOrNull(root, i).equals(new Node(i + 1)));
            }

            assert (LinkedList.getOrNull(root, -1) == null);
            assert (LinkedList.getOrNull(root, 10) == null);
        }

        {
            Node root = null;

            for (int i = 0; i < 10; ++i) {
                root = LinkedList.append(root, i + 1);
            }

            root = LinkedList.reverse(root);
            //LinkedList.testLinkedList(root, new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1});

            root = new Node(1);
            root = LinkedList.reverse(root);
            //LinkedList.testLinkedList(root, new int[]{1});

        }

        {
            Node node0 = null;
            Node node1 = null;


            for (int i = 0; i < 10; ++i) {
                if (i % 2 == 0) {
                    node0 = LinkedList.append(node0, i + 1);
                } else {
                    node1 = LinkedList.append(node1, i + 1);
                }
            }

            //LinkedList.testLinkedList(node0, new int[]{1, 3, 5, 7, 9});
            //LinkedList.testLinkedList(node1, new int[]{2, 4, 6, 8, 10});

            Node n = LinkedList.interleaveOrNull(node0, node1);
            //LinkedList.testLinkedList(n, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        }

        {
            Node node0 = null;
            Node node1 = null;


            for (int i = 0; i < 10; ++i) {
                if (i % 2 == 0) {
                    node0 = LinkedList.append(node0, i + 1);
                } else {
                    node1 = LinkedList.append(node1, i + 1);
                }
            }
        }

        {
            Node node0 = null;
            Node node1 = null;

            for (int i = 0; i < 20; ++i) {
                if (i % 2 == 0) {
                    node0 = LinkedList.append(node0, i + 1);
                }

            }

            for (int i = 0; i < 10; ++i) {
                if (i % 2 != 0) {
                    node1 = LinkedList.append(node1, i + 1);
                }
            }
            //LinkedList.testLinkedList(node0, new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19});
            //LinkedList.testLinkedList(node1, new int[]{2, 4, 6, 8, 10});


            Node n = LinkedList.interleaveOrNull(node0, node1);
            //LinkedList.testLinkedList(n, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19});
        }

        {
            Node node0 = null;
            Node node1 = null;

            for (int i = 0; i < 10; ++i) {
                if (i % 2 == 0) {
                    node0 = LinkedList.append(node0, i + 1);
                }
            }

            for (int i = 0; i < 20; ++i) {
                if (i % 2 != 0) {
                    node1 = LinkedList.append(node1, i + 1);
                }
            }
            //LinkedList.testLinkedList(node0, new int[]{1, 3, 5, 7, 9});
            //LinkedList.testLinkedList(node1, new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20});


            Node n = LinkedList.interleaveOrNull(node0, node1);
            //LinkedList.testLinkedList(n, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20});
        }

        System.out.println("------------------------------------------------------------------------");
    }


    public static void main(String[] args) {
        // write your code here
        TestLinkedList();
        testStack();
        testQueue();

        {
            Node node = null;
            node = LinkedList.append(null, 1);
            node = LinkedList.append(node, 2);
            node = LinkedList.append(node, 3);
            node = LinkedList.append(node, 4);
            node = LinkedList.append(node, 5);




        }

        {
            Node root = LinkedList.append(null, 10);

            root = LinkedList.append(root, 11);
            root = LinkedList.append(root, 12);

            assert (root.getData() == 10);

            Node next = root.getNextOrNull();

            assert (next.getData() == 11);

            next = next.getNextOrNull();

            assert (next.getData() == 12);
        }

        {
            Node root = LinkedList.append(null, 10);

            root = LinkedList.prepend(root, 11);

            assert (root.getData() == 11);

            root = LinkedList.prepend(root, 12);

            assert (root.getData() == 12);

            Node next = root.getNextOrNull();

            assert (next.getData() == 11);

            next = next.getNextOrNull();

            assert (next.getData() == 10);
        }

        {
            Node root = LinkedList.append(null, 10);

            root = LinkedList.insertAt(root, 0, 11);

            assert (root.getData() == 11);

            root = LinkedList.insertAt(root, 1, 12);

            assert (root.getData() == 11);

            Node next = root.getNextOrNull();

            assert (next.getData() == 12);

            next = next.getNextOrNull();

            assert (next.getData() == 10);
        }

        {
            Node root = LinkedList.append(null, 10);

            root = LinkedList.append(root, 11);
            root = LinkedList.append(root, 12);
            root = LinkedList.append(root, 13);

            root = LinkedList.removeAt(root, 0);

            assert (root.getData() == 11);

            root = LinkedList.removeAt(root, 1);

            assert (root.getData() == 11);

            Node next = root.getNextOrNull();

            assert (next.getData() == 13);
        }

        {
            Node root = LinkedList.append(null, 10);

            root = LinkedList.append(root, 11);

            int index = LinkedList.getIndexOf(root, 10);

            assert (index == 0);

            index = LinkedList.getIndexOf(root, 11);

            assert (index == 1);
        }

        {
            Node root = LinkedList.append(null, 10);

            root = LinkedList.append(root, 11);

            Node node = LinkedList.getOrNull(root, 0);

            assert (node.getData() == 10);

            node = LinkedList.getOrNull(root, 1);

            assert (node.getData() == 11);
        }

        {
            Node root1 = LinkedList.append(null, 10);

            root1 = LinkedList.append(root1, 11);
            root1 = LinkedList.append(root1, 12);

            Node root2 = LinkedList.append(null, 13);

            root2 = LinkedList.append(root2, 14);
            root2 = LinkedList.append(root2, 15);

            Node newRoot = LinkedList.interleaveOrNull(root1, root2); // newRoot: 10, list: 10 -> 13 -> 11 -> 14 -> 12 -> 15

            assert (newRoot.getData() == 10);

            Node next = newRoot.getNextOrNull();

            assert (next.getData() == 13);

            next = next.getNextOrNull();

            assert (next.getData() == 11);

            next = next.getNextOrNull();

            assert (next.getData() == 14);

            next = next.getNextOrNull();

            assert (next.getData() == 12);

            next = next.getNextOrNull();

            assert (next.getData() == 15);
        }

        {
            Node root = LinkedList.append(null, 10);

            root = LinkedList.append(root, 11);
            root = LinkedList.append(root, 12);
            root = LinkedList.append(root, 13);
            root = LinkedList.append(root, 14);

            root = LinkedList.reverse(root); // root: 14, list: 14 -> 13 -> 12 -> 11 -> 10

            assert (root.getData() == 14);

            Node next = root.getNextOrNull();

            assert (next.getData() == 13);

            next = next.getNextOrNull();

            assert (next.getData() == 12);

            next = next.getNextOrNull();

            assert (next.getData() == 11);

            next = next.getNextOrNull();

            assert (next.getData() == 10);
        }

        {
            Stack stack = new Stack();

            stack.push(20);
            stack.push(21); // stack: 21
            //        20

            int data = stack.pop();

            assert (data == 21);

            data = stack.pop();

            assert (data == 20);
        }

        {
            Stack stack = new Stack();

            stack.push(20); // stack: 20

            assert (stack.peek() == 20);

            stack.push(21); // stack: 21
            //        20

            assert (stack.peek() == 21);
        }

        {
            Stack stack  = new Stack();

            stack.push(20);
            stack.push(21);

            assert (stack.getSize() == 2);
        }

        {
            Queue queue = new Queue();

            queue.enqueue(20);

            assert (queue.peek() == 20);

            queue.enqueue(21);

            assert (queue.peek() == 20);
        }

        {
            Queue queue = new Queue();

            queue.enqueue(20);
            queue.enqueue(21);

            int data = queue.dequeue();

            assert (data == 20);

            data = queue.dequeue();

            assert (data == 21);
        }

        {
            Queue queue = new Queue();

            queue.enqueue(20);
            queue.enqueue(21);

            assert (queue.getSize() == 2);
        }
    }
}
