package academy.pocu.comp3500.lab6.app;

import academy.pocu.comp3500.lab6.League;
import academy.pocu.comp3500.lab6.leagueofpocu.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class Program {
    private static void test8() {
        Player player0 = new Player(8, "player0", 189);
        Player player0_1 = new Player(9, "player0", 700);
        Player player9 = new Player(7, "player9", 860);

        League league = new League(new Player[]{player0, player0_1});

        Player result1 = league.findMatchOrNull(player9);
        assert (result1 == player0_1);
    }

    public static void main(String[] args) {

        test8();
	    // write your code here
        {
            Player player1 = new Player(1, "player1", 9);
            Player player2 = new Player(2, "player2", 12);

            League league = new League(new Player[]{player1, player2});

            Player player1Match = league.findMatchOrNull(player1); // player4
            assert (player1Match == player2);
            Player player2Match = league.findMatchOrNull(player2); // player2
            assert (player2Match == player1);
        }

        {
            Player player1 = new Player(1, "player1", 12);
            Player player2 = new Player(2, "player2", 9);

            League league = new League(new Player[]{player1, player2});

            Player player1Match = league.findMatchOrNull(player1); // player4
            assert (player1Match == player2);
            Player player2Match = league.findMatchOrNull(player2); // player2
            assert (player2Match == player1);
        }


        {
            Program.joinTest();
            Program.leaveTest();
            Program.findMatchTest();
        }

        {
            Player player1 = new Player(1, "player1", 9);
            Player player2 = new Player(2, "player2", 12);
            Player player3 = new Player(3, "player3", 17);
            Player player4 = new Player(4, "player4", 14);

            League league = new League(new Player[]{player1, player2, player3, player4});

            Player player3Match = league.findMatchOrNull(player3); // player4
            assert (player3Match == player4);
            Player player4Match = league.findMatchOrNull(player4); // player2
            assert (player4Match == player2);
        }

        {
            Player player1 = new Player(1, "player1", 12);
            Player player2 = new Player(2, "player2", 17);
            Player player3 = new Player(3, "player3", 11);
            Player player4 = new Player(4, "player4", 18);
            Player player5 = new Player(5, "player5", 10);

            League league = new League(new Player[]{player1, player2, player3, player4, player5});

            Player[] topPlayers = league.getTop(3); // player4, player2, player1
            assert (topPlayers[0] == player4);
            assert (topPlayers[1] == player2);
            assert (topPlayers[2] == player1);

        }

        {
            Player player1 = new Player(1, "player1", 12);
            Player player2 = new Player(2, "player2", 17);
            Player player3 = new Player(3, "player3", 11);
            Player player4 = new Player(4, "player4", 18);
            Player player5 = new Player(5, "player5", 10);

            League league = new League(new Player[]{player1, player2, player3, player4, player5});

            Player[] bottomPlayers = league.getBottom(3); // player5, player3, player1
            assert (bottomPlayers[0] == player5);
            assert (bottomPlayers[1] == player3);
            assert (bottomPlayers[2] == player1);
        }
        {
            Player player1 = new Player(1, "player1", 12);
            Player player2 = new Player(2, "player2", 15);

            League league = new League(new Player[]{player1, player2});

            Player newPlayer = new Player(3, "player3", 13);

            boolean success = league.join(newPlayer); // true
            assert (success);
            success = league.join(newPlayer); // false
            assert (!success);
            success = league.join(player2); // false
            assert (!success);
        }

        {
            Player player1 = new Player(1, "player1", 12);
            Player player2 = new Player(2, "player2", 13);
            Player player3 = new Player(3, "player3", 15);

            League league = new League(new Player[]{player1, player2, player3});

            boolean success = league.leave(player1); // true
            assert (success);
            success = league.leave(player2); // true
            assert (success);
            success = league.leave(player1); // false
            assert (!success);

        }

    }

    public static void joinTest() {
        final int playerCount = 100;
        Player[] players = new Player[playerCount];

        HashSet<Integer> uniqueRatings = new HashSet<>();
        Random generator = new Random();

        while (uniqueRatings.size() < playerCount) {
            int low = 10;
            int high = 1000;
            uniqueRatings.add(generator.nextInt(high - low) + low);
        }

        ArrayList<Integer> randomN = new ArrayList<>(uniqueRatings);
        for (int i = 0; i < playerCount; ++i) {
            players[i] = new Player(i, "", randomN.get(i));
        }

        League league = new League(players);
        ArrayList<Integer> expected = new ArrayList<>(uniqueRatings);
        Collections.sort(expected);

        Player[] actual = league.getBottom(playerCount); // higher rating -> top
        for (int i = 0; i < playerCount; ++i) {
            assert (expected.get(i) == actual[i].getRating());
        }

        ArrayList<Integer> expected2 = new ArrayList<>(uniqueRatings);
        Collections.sort(expected2, Collections.reverseOrder());
        actual = league.getTop(playerCount);

        for (int i = 0; i < playerCount; ++i) {
            assert (expected2.get(i) == actual[i].getRating());
        }
    }

    public static void leaveTest() { // delete from head
        final int playerCount = 50;
        Player[] players = new Player[playerCount];
        HashSet<Integer> uniqueRatings = new HashSet<>();
        Random generator = new Random(1);
        while (uniqueRatings.size() < playerCount) {
            int low = 10;
            int high = 1000;
            uniqueRatings.add(generator.nextInt(high - low) + low);
        }
        ArrayList<Integer> randomN = new ArrayList<>(uniqueRatings);
        ArrayList<Integer> p = new ArrayList<>(1024);
        for (int i = 0; i < playerCount; ++i) {
            players[i] = new Player(i, "", randomN.get(i));
            p.add(players[i].getRating());
        }
        League league = new League(players);
        for (int i = 0; i < playerCount; ++i) {
            if (i == 36) {
                System.out.println();
            }
            league.leave(players[i]);
            p.remove(0);
            // test equality
            ArrayList<Integer> expected = new ArrayList<>(p);
            Collections.sort(expected);
            Player[] actual = league.getBottom(p.size()); // higher rating -> top
            for (int j = 0; j < p.size(); ++j) {
                assert (expected.get(j) == actual[j].getRating());
            }
        }
    }
    public static void findMatchTest() {
        final int playerCount = 100;
        Player[] players = new Player[playerCount];
        HashSet<Integer> uniqueRatings = new HashSet<>();
        Random generator = new Random(1);
        while (uniqueRatings.size() < playerCount) {
            int low = 10;
            int high = 1000;
            uniqueRatings.add(generator.nextInt(high - low) + low);
        }
        ArrayList<Integer> randomN = new ArrayList<>(uniqueRatings);
        for (int i = 0; i < playerCount; ++i) {
            players[i] = new Player(i, "", randomN.get(i));
        }
        League league = new League(players);
        ArrayList<Integer> sorted = new ArrayList<>(randomN);
        Collections.sort(sorted);
        for (int i = 0; i < playerCount; ++i) {
            int rating = players[i].getRating();
            int expectedRating = getMatch(sorted, rating);
            assert (expectedRating != -1);
            if (i == 9) {
                System.out.println();
            }

            Player match = league.findMatchOrNull(players[i]);
            int actualRating = match.getRating();
            assert (expectedRating == actualRating);
        }
    }
    public static int getMatch(ArrayList<Integer> sorted, int targetRating) {
        // targetRating이 항상 존재한다고 가정

        int left = 0, right = sorted.size() - 1;
        int targetIndex = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (sorted.get(mid) == targetRating) {
                targetIndex = mid;
                break;
            }

            if (targetRating > sorted.get(mid)) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        assert (targetIndex != -1);
        if (targetIndex == 0) {
            return sorted.get(1);
        }
        if (targetIndex == sorted.size() - 1) {
            return sorted.get(sorted.size() - 2);
        }
        int distanceToLeft = Math.abs(sorted.get(targetIndex - 1) - targetRating);
        int distanceToRight = Math.abs(sorted.get(targetIndex + 1) - targetRating);
        return distanceToLeft < distanceToRight ? sorted.get(targetIndex - 1) : sorted.get(targetIndex + 1);
    }
}
