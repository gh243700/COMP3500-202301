package academy.pocu.comp3500.assignment1.app;

import academy.pocu.comp3500.assignment1.PocuBasketballAssociation;
import academy.pocu.comp3500.assignment1.pba.Player;

public class Program {

    public static int findDreamTeamSize(final Player[] players, final Player[] scratch) {

        long maxTeamwork = 0;
        int bestTeamSize = 0;

        //sortPlayersByPassPerGame(players, 0, players.length - 1);

        for (int i = 0; i < players.length; ++i) {
            for (int k = 0; k < players.length - 1 - i; ++k) {
                if (players[k].getPassesPerGame() < players[k + 1].getPassesPerGame()) {
                    Player temp = players[k];
                    players[k] = players[k + 1];
                    players[k + 1] = temp;
                }
            }
            scratch[i] = players[players.length - 1 - i];
        }

        //sortPlayersByAssistsPerGame(scratch, 0, scratch.length - 1);

        for (int i = 0; i < scratch.length; ++i) {
            int size = 0;

            int sum = 0;

            boolean isAssistValueCounted = false;
            Player forAssistValue = scratch[i];

            final int MAX_SIZE = i;

            int index = 0;
            while (index < players.length) {

                if (players[index].getAssistsPerGame() >= forAssistValue.getAssistsPerGame()) {
                    if (players[index] != forAssistValue) {
                        sum += players[index].getPassesPerGame();
                        ++size;
                    }

                    if (!isAssistValueCounted) {
                        isAssistValueCounted = true;
                        ++size;
                    }

                    long tempTeamwork = (long) ((sum + forAssistValue.getPassesPerGame()) * (double) forAssistValue.getAssistsPerGame());

                    if (maxTeamwork < tempTeamwork) {
                        maxTeamwork = tempTeamwork;
                        bestTeamSize = size;
                    }

                    if (MAX_SIZE < size)
                    {
                        break;
                    }
                }
                ++index;
            }
        }

        return bestTeamSize;
    }

    public static void TestFindPlayerPointsPerGame()
    {
        {
            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 0),
                    new Player("Player 2", 1, 5, 1, 0),
                    new Player("Player 3", 5, 5, 1, 0),
                    new Player("Player 4", 10, 5, 1, 0),
                    new Player("Player 5", 15, 5, 1, 0),
                    new Player("Player 6", 30, 5, 1, 0),
                    new Player("Player 7", 30, 5, 1, 0),
                    new Player("Player 8", 30, 5, 1, 0),
                    new Player("Player 9", 41, 5, 1, 0),
                    new Player("Player 10", 50, 5, 1, 0),
            };


            Player player = PocuBasketballAssociation.findPlayerPointsPerGame(players, 100);
            assert (player.equals(players[9]));

        }

        {
            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 0),
                    new Player("Player 2", 1, 5, 1, 0),
                    new Player("Player 3", 5, 5, 1, 0),
                    new Player("Player 4", 10, 5, 1, 0),
                    new Player("Player 5", 15, 5, 1, 0),
                    new Player("Player 6", 30, 5, 1, 0),
                    new Player("Player 7", 30, 5, 1, 0),
                    new Player("Player 8", 30, 5, 1, 0),
                    new Player("Player 9", 41, 5, 1, 0),
                    new Player("Player 10", 50, 5, 1, 0),
            };

            for (int i = 0; i < 100; ++i)
            {
                Player player = PocuBasketballAssociation.findPlayerPointsPerGame(players, i);
                if (i < 3) // player 1
                {
                    assert (player.equals(players[1]));
                } else if (i <= 7) // player 2
                {
                   assert (player.equals(players[2]));
                } else if (i <= 12)
                {
                    assert (player.equals(players[3]));
                }
                else if (i <= 22) //// player 3
                {
                    assert (player.equals(players[4]));
                } else if (i <= 35)
                {
                    assert (player.equals(players[7]));
                } else if (i <= 45)
                {
                    assert (player.equals(players[8]));
                } else
                {
                    assert (player.equals(players[9]));
                }

            }

            System.out.println("sssss");
        }
    }

    public static void TestFind3ManDreamTeam()
    {
        Player[] players = new Player[] {
                new Player("Player 2", 5, 12, 14, 50),
                new Player("Player 6", 15, 2, 5, 40),
                new Player("Player 5", 11, 1, 11, 54),
                new Player("Player 4", 10, 3, 51, 88),
                new Player("Player 7", 16, 8, 5, 77),
                new Player("Player 1", 1, 15, 2, 22),
                new Player("Player 3", 7, 5, 8, 66)
        };

        Player[] outPlayers = new Player[3];
        Player[] scratch = new Player[3];

        long maxTeamwork = PocuBasketballAssociation.find3ManDreamTeam(players, outPlayers, scratch); // maxTeamwork: 219, outPlayers: [ Player 4, Player 2, Player 3 ]

        for (int i = 0; i < outPlayers.length; ++i)
        {
            System.out.println(outPlayers[i]);
        }
        assert (maxTeamwork == 219);

    }

    public static void main(String[] args) {
        {
            TestFindPlayerPointsPerGame();
            TestFind3ManDreamTeam();
        }

        {
            Player[] players = new Player[] {
                    new Player("Player 2", 5, 5, 17, 50),
                    new Player("Player 6", 15, 4, 10, 40),
                    new Player("Player 5", 11, 3, 25, 54),
                    new Player("Player 4", 10, 9, 1, 88),
                    new Player("Player 7", 16, 7, 5, 77),
                    new Player("Player 1", 1, 2, 8, 22),
                    new Player("Player 9", 42, 15, 4, 56),
                    new Player("Player 8", 33, 11, 3, 72),
            };

            int k = 4;
            Player[] outPlayers = new Player[4];
            Player[] scratch = new Player[k];

            long maxTeamwork = PocuBasketballAssociation.findDreamTeam(players, k, outPlayers, scratch);
            System.out.println("-------------------------------------------------------------");
            for (int i = 0; i < outPlayers.length; ++i)
            {
                System.out.println(players[i]);
            }

            assert (maxTeamwork == 171);


        }

        {
            Player[] players = new Player[] {

                    new Player("Player 6", 7, 2, 10, 15),
                    new Player("Player 7", 8, 15, 3, 11),
                    new Player("Player 8", 5, 7, 13, 5),
                    new Player("Player 9", 8, 2, 7, 67),
                    new Player("Player 10", 1, 11, 1, 29),
                    new Player("Player 11", 2, 6, 9, 88),
                    new Player("Player 1", 2, 5, 10, 78),
                    new Player("Player 2", 10, 4, 5, 66),
                    new Player("Player 3", 3, 3, 2, 22),
                    new Player("Player 4", 1, 9, 8, 12),
                    new Player("Player 5", 11, 1, 12, 26),
            };

            Player[] scratch = new Player[players.length];

            int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 6

            assert (k == 6);


        }


        {
            Player[] players = new Player[] {
                    new Player("Player 9", 8, 2, 7, 67),
                    new Player("Player 10", 1, 11, 1, 29),
                    new Player("Player 11", 2, 6, 9, 88),
                    new Player("Player 1", 2, 5, 10, 78),
                    new Player("Player 2", 10, 4, 5, 66),
                    new Player("Player 3", 3, 3, 2, 22),
                    new Player("Player 4", 1, 9, 8, 12),
                    new Player("Player 5", 11, 1, 12, 26),
                    new Player("Player 6", 7, 2, 10, 15),
                    new Player("Player 7", 8, 15, 3, 11),
                    new Player("Player 8", 5, 7, 13, 5),
            };

            Player[] scratch = new Player[players.length];

            int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 6

            assert (k == 6);

        }


        {
            Player[] players = new Player[] {
                    new Player("Player 9", 8, 1, 0, 67),
                    new Player("Player 10", 1, 2, 0, 29),
                    new Player("Player 11", 2, 3, 0, 88),
                    new Player("Player 1", 2, 4, 0, 78),
                    new Player("Player 2", 10, 5, 0, 66),
                    new Player("Player 3", 3, 6, 0, 22),
                    new Player("Player 4", 1, 7, 0, 12),
                    new Player("Player 5", 11, 8, 0, 26),
                    new Player("Player 6", 7, 9, 50, 15),
                    new Player("Player 7", 8, 10, 0, 11),
                    new Player("Player 8", 5, 11, 13, 5),
            };

            Player[] scratch = new Player[players.length];

            int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 6

            assert (k == 2);
        }

        {
            Player[] players = new Player[] {
                    new Player("Player 9", 8, 1, 5, 67),
            };

            Player[] scratch = new Player[players.length];

            int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 6

            assert (k == 1);
        }

        {
            Player[] players = new Player[] {
                    new Player("Player 9", 8, 0, 10000, 67),
                    new Player("Player 10", 1, 2, 0, 29),
                    new Player("Player 11", 2, 3, 0, 88),
                    new Player("Player 1", 2, 4, 0, 78),
                    new Player("Player 2", 10, 5, 0, 66),
                    new Player("Player 3", 3, 6, 0, 22),
                    new Player("Player 4", 1, 7, 0, 12),
                    new Player("Player 5", 11, 8, 0, 26),
                    new Player("Player 6", 7, 9, 50, 15),
                    new Player("Player 7", 8, 10, 100, 11),
                    new Player("Player 8", 5, 11, 13, 5),
            };

            Player[] scratch = new Player[players.length];

            int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 6

            assert (k == 3);
        }


    }
}
