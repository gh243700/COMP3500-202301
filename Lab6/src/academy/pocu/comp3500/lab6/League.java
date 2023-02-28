package academy.pocu.comp3500.lab6;

import academy.pocu.comp3500.lab6.leagueofpocu.Player;

public class League {
    private PlayerBST playerBST;

    public League() {
        playerBST = new PlayerBST();
    }

    public League(Player[] players) {
        playerBST = new PlayerBST(players);
    }

    public Player findMatchOrNull(final Player player) {
        return playerBST.findClosestScoreOrNull(player);
    }

    public Player[] getTop(final int count) {
        return playerBST.getByOrder(count, PlayerBST.OrderMode.MAX);
    }

    public Player[] getBottom(final int count) {
        return playerBST.getByOrder(count, PlayerBST.OrderMode.MIN);
    }

    public boolean join(final Player player) {
        return playerBST.insert(player);
    }

    public boolean leave(final Player player) {
        return playerBST.delete(player);
    }
}