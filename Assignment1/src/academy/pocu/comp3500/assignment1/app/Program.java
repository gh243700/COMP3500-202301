package academy.pocu.comp3500.assignment1.app;

import academy.pocu.comp3500.assignment1.PocuBasketballAssociation;
import academy.pocu.comp3500.assignment1.pba.GameStat;
import academy.pocu.comp3500.assignment1.pba.Player;

import java.util.Random;

public class Program {

    /*
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

 */

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
            for (int j = 0; j < 100; j++)
            {
                int length = (int) (Math.random() * 10);
                Player[] players = new Player[length];
                Random random0 = new Random();
                Random random1 = new Random();
                System.out.println("Player[] players = {");
                for (int i = 0; i < length; i++) {
                    players[i] = new Player(String.format("Player %d", i), 0, random0.nextInt(100), random1.nextInt(100), 0);
                    System.out.println("Player players"+ i +" = new Player(\" "+players[i].getName() + "\" ,"+ 0 +","+ players[i].getAssistsPerGame()+","+ ((i % 4 == 0) ? 0 : players[i].getPassesPerGame())+","+ 0 + "),");
                }
                System.out.println("};");

                Player[] scratch = new Player[length];

                long k = PocuBasketballAssociation.findDreamTeamSize(players, scratch);

                Player[] scratchAnswer = new Player[length];
                //long answer = findDreamTeamSize(players,  scratchAnswer);
                //assert answer == k;

                System.out.println("answer == " + k);
            }

        }

/*


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

        {
            GameStat[] gameStats = new GameStat[] {
                    new GameStat("Player 1", 1, 13, 5, 6, 10, 1),
                    new GameStat("Player 2", 2, 5, 2, 5, 0, 10),
                    new GameStat("Player 1", 3, 12, 6, 9, 8, 5),
                    new GameStat("Player 3", 1, 31, 15, 40, 5, 3),
                    new GameStat("Player 2", 1, 3, 1, 3, 12, 2),
                    new GameStat("Player 1", 2, 11, 6, 11, 9, 3),
                    new GameStat("Player 2", 3, 9, 3, 3, 1, 11),
                    new GameStat("Player 3", 4, 32, 15, 51, 4, 2),
                    new GameStat("Player 4", 3, 44, 24, 50, 1, 1),
                    new GameStat("Player 1", 4, 11, 5, 14, 8, 3),
                    new GameStat("Player 2", 4, 5, 1, 3, 1, 9),
            };

            Player[] players = new Player[] {
                    new Player(),
                    new Player(),
                    new Player(),
                    new Player()
            };

            PocuBasketballAssociation.processGameStats(gameStats, players);
/*
players: [
    { "Player 2", pointsPerGame: 5, assistsPerGame: 3, passesPerGame: 8, shootingPercentage: 50 },
    { "Player 1", pointsPerGame: 11, assistsPerGame: 8, passesPerGame: 3, shootingPercentage: 55 },
    { "Player 4", pointsPerGame: 44, assistsPerGame: 1, passesPerGame: 1, shootingPercentage: 48 },
    { "Player 3", pointsPerGame: 31, assistsPerGame: 4, passesPerGame: 2, shootingPercentage: 32 }
]



            System.out.println("--");
        }*/
    }
}
