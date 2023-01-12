package academy.pocu.comp3500.lab2;

import academy.pocu.comp3500.lab2.datastructure.Node;

public final class LinkedList {
    private LinkedList() {
    }

    public static void testLinkedList(Node root, int[] arr)
    {
        Node node = root;
        int i = 0;
        while (node != null)
        {
            assert (node.getData() == arr[i]);
            ++i;
            node = node.getNextOrNull();
        }

        assert (i == arr.length);
    }

    public static void printLinkedList(Node root)
    {
        Node node = root;
        System.out.println("---------------------------------------------------------------------");
        while (node != null)
        {
            System.out.print(node.getData() + ", ");
            node = node.getNextOrNull();
        }
        System.out.println("\n---------------------------------------------------------------------");

    }



    public static Node append(final Node rootOrNull, final int data) {

        if (rootOrNull == null) {
            return new Node(data);
        }

        Node node = rootOrNull;

        while (node.getNextOrNull() != null) {
            node = node.getNextOrNull();
        }

        node.setNext(new Node(data));

        return rootOrNull;
    }

    public static Node prepend(final Node rootOrNull, final int data) {
        Node newRoot = new Node(data);
        newRoot.setNext(rootOrNull);

        return newRoot;
    }

    public static Node insertAt(final Node rootOrNull, final int index, final int data) {

        if (index < 0 || rootOrNull == null) {
            return rootOrNull;
        }

        if (index == 0) {
            Node newRootNode = new Node(data);
            newRootNode.setNext(rootOrNull);
            return newRootNode;
        }

        Node nodeBeforeNodeAtIndex = rootOrNull;

        int nodeIndex = 0;
        while (nodeBeforeNodeAtIndex != null) {
            if (nodeIndex >= index - 1) {
                break;
            }
            nodeBeforeNodeAtIndex = nodeBeforeNodeAtIndex.getNextOrNull();
            ++nodeIndex;
        }

        if (nodeBeforeNodeAtIndex == null) {
            return rootOrNull;
        }

        Node newNode = new Node(data);
        newNode.setNext(nodeBeforeNodeAtIndex.getNextOrNull());
        nodeBeforeNodeAtIndex.setNext(newNode);

        return rootOrNull;
    }

    public static Node removeAt(final Node rootOrNull, final int index) {

        if (index < 0 || rootOrNull == null) {
            return rootOrNull;
        }

        if (index == 0) {
            Node newRootOrNull = rootOrNull.getNextOrNull();
            rootOrNull.setNext(null);
            return newRootOrNull;
        }

        Node nodeBeforeNodeAtIndex = rootOrNull;

        int nodeIndex = 0;
        while (nodeBeforeNodeAtIndex != null) {
            if (nodeIndex >= index - 1) {
                break;
            }
            nodeBeforeNodeAtIndex = nodeBeforeNodeAtIndex.getNextOrNull();
            ++nodeIndex;
        }

        if (nodeBeforeNodeAtIndex == null) {
            return rootOrNull;
        }

        Node nodeAtIndex = nodeBeforeNodeAtIndex.getNextOrNull();

        if (nodeAtIndex != null) {
            nodeBeforeNodeAtIndex.setNext(nodeAtIndex.getNextOrNull());
            nodeAtIndex.setNext(null);
        }

        return rootOrNull;
    }

    public static int getIndexOf(final Node rootOrNull, final int data) {

        Node node = rootOrNull;

        int index = 0;
        while (node != null) {
            if (data == node.getData()) {
                return index;
            }

            node = node.getNextOrNull();
            ++index;
        }

        return -1;
    }

    public static Node getOrNull(final Node rootOrNull, final int index) {
        Node node = rootOrNull;

        int at = 0;
        while (node != null) {
            if (at == index) {
                return node;
            }

            node = node.getNextOrNull();
            ++at;
        }

        return null;
    }

    public static Node reverse(final Node rootOrNull) {

        if (rootOrNull == null) {
            return null;
        }

        Node before = null;
        Node next = rootOrNull;

        while (next != null) {
            Node temp = next.getNextOrNull();
            next.setNext(before);

            before = next;
            next = temp;
        }

        return before;
    }

    public static Node interleaveOrNull(final Node root0OrNull, final Node root1OrNull) {
        if (root0OrNull == null) {
            return root1OrNull;
        }

        if (root1OrNull == null) {
            return root0OrNull;
        }

        Node nodeFront = root0OrNull;
        Node nodeBack = root1OrNull;

        while (nodeBack != null) {
            Node temp = nodeFront.getNextOrNull();
            nodeFront.setNext(nodeBack);

            nodeFront = nodeBack;
            nodeBack = temp;
        }

        return root0OrNull;
    }
}