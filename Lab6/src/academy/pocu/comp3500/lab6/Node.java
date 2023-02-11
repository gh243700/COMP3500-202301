package academy.pocu.comp3500.lab6;

import academy.pocu.comp3500.lab6.leagueofpocu.Player;

public class Node {
    private Player player;
    private Node left;
    private Node right;

    public Node(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int compareRating(Node other) {
        if (this.player.getRating() > other.player.getRating()) {
            return 1;
        }

        return this.player.getRating() == other.player.getRating() ? 0 : -1;
    }

    public int compareRating(Player other) {
        if (this.player.getRating() > other.getRating()) {
            return 1;
        }

        return this.player.getRating() == other.getRating() ? 0 : -1;
    }

    public int compareSubtractAbs(Player other) {
        return Math.abs(player.getRating() - other.getRating());
    }

    public boolean isEqualScore(Player player) {
        return player.getId() == player.getId();
    }
}
