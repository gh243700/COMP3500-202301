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

            LinkedList.testLinkedList(root, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        }
        {
            Node root = null;

            root = LinkedList.prepend(root, 10);
            LinkedList.testLinkedList(root, new int[]{10});
            root = LinkedList.prepend(root, 9);
            root = LinkedList.prepend(root, 8);
            root = LinkedList.prepend(root, 7);
            root = LinkedList.prepend(root, 6);
            root = LinkedList.prepend(root, 5);
            root = LinkedList.prepend(root, 4);
            root = LinkedList.prepend(root, 3);
            root = LinkedList.prepend(root, 2);
            root = LinkedList.prepend(root, 1);

            LinkedList.testLinkedList(root, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        }

        {//insertAt
            Node root = null;

            for (int i = 0; i < 10; ++i) {
                if (i % 2 == 0) {
                    root = LinkedList.append(root, i + 1);
                }
            }

            LinkedList.testLinkedList(root, new int[]{1, 3, 5, 7, 9});

            for (int i = 0; i < 11; ++i) {
                if (i % 2 == 0) {
                    root = LinkedList.insertAt(root, i, i);
                }

            }
            LinkedList.testLinkedList(root, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});


            root = LinkedList.insertAt(root, -1, 100);
            LinkedList.testLinkedList(root, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});


            root = LinkedList.insertAt(root, 12, 100);
            LinkedList.testLinkedList(root, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        }

        { //removeAt
            Node root = null;

            for (int i = 0; i < 10; ++i) {
                root = LinkedList.append(root, i + 1);
            }

            LinkedList.testLinkedList(root, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

            root = LinkedList.removeAt(root, 0);
            LinkedList.testLinkedList(root, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10});

            root = LinkedList.removeAt(root, 8);
            LinkedList.testLinkedList(root, new int[]{2, 3, 4, 5, 6, 7, 8, 9});

            root = LinkedList.removeAt(root, 2);
            LinkedList.testLinkedList(root, new int[]{2, 3, 5, 6, 7, 8, 9});

            root = LinkedList.removeAt(root, -1);
            LinkedList.testLinkedList(root, new int[]{2, 3, 5, 6, 7, 8, 9});


            root = LinkedList.removeAt(root, 7);
            LinkedList.testLinkedList(root, new int[]{2, 3, 5, 6, 7, 8, 9});


            root = LinkedList.removeAt(root, 6);
            LinkedList.testLinkedList(root, new int[]{2, 3, 5, 6, 7, 8});
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
            LinkedList.testLinkedList(root, new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1});

            root = new Node(1);
            root = LinkedList.reverse(root);
            LinkedList.testLinkedList(root, new int[]{1});

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

            LinkedList.testLinkedList(node0, new int[]{1, 3, 5, 7, 9});
            LinkedList.testLinkedList(node1, new int[]{2, 4, 6, 8, 10});

            Node n = LinkedList.interleaveOrNull(node0, node1);
            LinkedList.testLinkedList(n, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
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
            LinkedList.testLinkedList(node0, new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19});
            LinkedList.testLinkedList(node1, new int[]{2, 4, 6, 8, 10});


            Node n = LinkedList.interleaveOrNull(node0, node1);
            LinkedList.testLinkedList(n, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19});
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
            LinkedList.testLinkedList(node0, new int[]{1, 3, 5, 7, 9});
            LinkedList.testLinkedList(node1, new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20});


            Node n = LinkedList.interleaveOrNull(node0, node1);
            LinkedList.testLinkedList(n, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20});
        }

        System.out.println("------------------------------------------------------------------------");
    }


    public static void main(String[] args) {
        // write your code here
        TestLinkedList();
        testStack();
        testQueue();
    }
}
