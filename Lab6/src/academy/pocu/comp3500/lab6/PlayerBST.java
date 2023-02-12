package academy.pocu.comp3500.lab6;

import academy.pocu.comp3500.lab6.leagueofpocu.Player;

public final class PlayerBST {
    private Node root;

    private enum InOrderMode {SUCCESSOR, PREDECESSOR, NONE}

    public enum OrderMode {MAX, MIN}

    private int joinedCount;

    public PlayerBST() {
    }

    public PlayerBST(final Player[] players) {
        for (Player player : players) {
            insert(player);
        }
    }

    public Player findClosestScoreOrNull(final Player player) {
        Node[] result = new Node[1];
        findClosestScoreRecursive(root, player, result, InOrderMode.NONE);

        return (result[0] != null) ? result[0].getPlayer() : null;
    }

    private void findClosestScoreRecursive(final Node node, final Player target, final Node[] out, final InOrderMode eMode) {
        if (node == null) {
            return;
        }

        if (node.compareRating(target) == 0) {
            findClosestScoreRecursive(node.getLeft(), target, out, InOrderMode.PREDECESSOR);
            findClosestScoreRecursive(node.getRight(), target, out, InOrderMode.SUCCESSOR);
            return;
        }

        if (out[0] == null || node.ratingAbsMargin(target) < out[0].ratingAbsMargin(target)) {
            out[0] = node;
        } else if (node.ratingAbsMargin(target) == out[0].ratingAbsMargin(target)) {
            out[0] = node.compareRating(out[0]) >= 1 ? node : out[0];
        }

        if (eMode == InOrderMode.SUCCESSOR || node.compareRating(target) > 0) {
            findClosestScoreRecursive(node.getLeft(), target, out, eMode);
            return;
        }
        findClosestScoreRecursive(node.getRight(), target, out, eMode);
    }

    public boolean delete(final Player player) {
        NodeWrapper wrapper = new NodeWrapper();
        if (findPlayerNode(null, root, player, wrapper) == false) {
            return false;
        }

        --joinedCount;

        Node child = wrapper.getChild();
        Node parent = wrapper.getParent();

        if (child.getLeft() == null && child.getRight() == null) {
            if (parent == null) {
                root = null;
                return true;
            }

            if (parent.getLeft() == child) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }

            return true;
        }
        //assert leaf exists
        deleteRecursive(wrapper);
        return true;
    }

    private void deleteRecursive(final NodeWrapper playerNodeWrapper) {

        Node playerNode = playerNodeWrapper.getChild();
        Node playerNodeParentOrNull = playerNodeWrapper.getParent();

        NodeWrapper leafNodeWrapper = new NodeWrapper();
        if (findDeletableLeaf(playerNode, leafNodeWrapper)) {
            Node leaf = leafNodeWrapper.getChild();
            Node leafParent = leafNodeWrapper.getParent();

            playerNode.setPlayer(leaf.getPlayer());
            if (leafNodeWrapper.isChildAtRightSideOfParent()) {
                leafParent.setRight(null);
            } else {
                leafParent.setLeft(null);
            }

            return;
        }
        //assert did not find deletable leaf

        Node shoudBeNull = playerNode.getRight();
        Node smallistRight = playerNode;

        while (shoudBeNull != null) {
            smallistRight = shoudBeNull;
            shoudBeNull = shoudBeNull.getLeft();
        }

        if (smallistRight == playerNode) {
            playerNode.setRight(playerNode.getLeft());
        } else {
            smallistRight.setLeft(playerNode.getLeft());
        }

        if (playerNodeParentOrNull == null) {
            root = playerNode.getRight();
        } else if (playerNodeWrapper.isChildAtRightSideOfParent()) {
            playerNodeParentOrNull.setRight(playerNode.getRight());
        } else {
            playerNodeParentOrNull.setLeft(playerNode.getRight());
        }
    }

    private boolean findDeletableLeaf(final Node node, final NodeWrapper wrapper) {

        for (int i = 0; i < 2; ++i) {
            Node subNode = (i == 0) ? node.getRight() : node.getLeft();
            Node parent = node;
            while (subNode != null) {
                if (subNode.getRight() == null && subNode.getLeft() == null) {
                    wrapper.setChild(subNode);
                    wrapper.setParent(parent);
                    return true;
                }

                parent = subNode;
                subNode = (i == 0) ? subNode.getLeft() : subNode.getRight();
            }
        }

        return false;
    }

    private boolean findPlayerNode(final Node parent, final Node node, final Player player, final NodeWrapper wrapper) {
        if (node == null) {
            return false;
        }

        if (node.isEqual(player)) {
            wrapper.setParent(parent);
            wrapper.setChild(node);
            return true;
        }

        if (node.compareRating(player) > 0) {
            return findPlayerNode(node, node.getLeft(), player, wrapper);
        }

        return findPlayerNode(node, node.getRight(), player, wrapper);
    }

    public boolean insert(final Player player) {
        if (root == null) {
            ++joinedCount;
            root = new Node(player);
            return true;
        }

        return insertRecursive(root, player);
    }

    private boolean insertRecursive(final Node node, final Player player) {

        Node subNode;
        boolean bAddToLeft = false;

        if (node.isEqual(player) || node.compareRating(player) == 0) {
            return false;
        }

        if (node.compareRating(player) < 0) {
            subNode = node.getRight();
        } else {
            bAddToLeft = true;
            subNode = node.getLeft();
        }

        if (subNode == null) {
            if (bAddToLeft) {
                node.setLeft(new Node(player));
            } else {
                node.setRight(new Node(player));
            }

            ++joinedCount;
            return true;
        }

        return insertRecursive(subNode, player);
    }

    public Player[] getByOrder(final int count, final OrderMode mode) {
        Player[] result = new Player[joinedCount >= count ? count : joinedCount];
        getByOrderRecursive(root, result, new int[1], mode);

        return result;
    }

    private void getByOrderRecursive(final Node node, final Player[] out, final int[] outCount, final OrderMode mode) {
        if (node == null || outCount[0] >= out.length) {
            return;
        }

        getByOrderRecursive((mode == OrderMode.MAX) ? node.getRight() : node.getLeft(), out, outCount, mode);

        if (outCount[0] < out.length) {
            out[outCount[0]] = node.getPlayer();
            ++outCount[0];
        }

        getByOrderRecursive((mode == OrderMode.MAX) ? node.getLeft() : node.getRight(), out, outCount, mode);
    }
}
