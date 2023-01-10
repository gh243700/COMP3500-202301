package academy.pocu.comp3500.assignment1.app;

import academy.pocu.comp3500.assignment1.PocuBasketballAssociation;
import academy.pocu.comp3500.assignment1.pba.Player;

public class Program {

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

            for (int i = 0; i < 50; ++i)
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
                }

            }


        }
    }


    public static void main(String[] args) {
        {
            TestFindPlayerPointsPerGame();
        }
    }
}
