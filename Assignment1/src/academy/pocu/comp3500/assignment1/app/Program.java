package academy.pocu.comp3500.assignment1.app;

import academy.pocu.comp3500.assignment1.PocuBasketballAssociation;
import academy.pocu.comp3500.assignment1.pba.Player;

public class Program {

    public static void main(String[] args) {
        {
            {
                Player[] players = new Player[] {
                        new Player("Player 4", 10, 10, 15, 25),
                        new Player("Player 2", 5, 2, 11, 31),
                        new Player("Player 3", 7, 4, 7, 44),
                        new Player("Player 1", 1, 5, 1, 60),
                        new Player("Player 6", 15, 0, 12, 61),
                        new Player("Player 8", 15, 0, 1, 61),
                        new Player("Player 7", 16, 8, 2, 70),
                        new Player("Player 5", 11, 12, 6, 77)
                };

                Player player = PocuBasketballAssociation.findPlayerShootingPercentage(players, 70); // player: Player 2
                assert (player.equals(players[6]));
                player = PocuBasketballAssociation.findPlayerShootingPercentage(players, 60); // player: Player 1
                assert (player.equals(players[3]));
                player = PocuBasketballAssociation.findPlayerShootingPercentage(players, 61); // player: Player 7
                assert (player.equals(players[5]));

                player = PocuBasketballAssociation.findPlayerShootingPercentage(players, 77); // player: Player 7
                assert (player.equals(players[7]));
            }


            {
                Player[] players = new Player[] {
                        new Player("Player 1", 1, 5, 1, 60),
                        new Player("Player 2", 5, 2, 11, 31),
                        new Player("Player 3", 7, 4, 7, 44),
                        new Player("Player 4", 10, 10, 15, 25),
                        new Player("Player 5", 11, 12, 6, 77),
                        new Player("Player 6", 15, 0, 12, 61),
                        new Player("Player 7", 16, 8, 2, 70),
                        new Player("Player 8", 19, 8, 2, 70),
                        new Player("Player 9", 20, 8, 2, 70),
                        new Player("Player 10", 21, 8, 2, 70),
                        new Player("Player 11", 22, 8, 2, 70),
                        new Player("Player 12", 40, 8, 2, 70),
                        new Player("Player 13", 40, 8, 2, 70),
                        new Player("Player 14", 41, 8, 2, 70),
                        new Player("Player 15", 42, 8, 2, 70),
                        new Player("Player 16", 43, 8, 2, 70),
                };

                Player player = PocuBasketballAssociation.findPlayerPointsPerGame(players, 43);
                System.out.println(player);
                assert (player.equals(players[15]));

                player = PocuBasketballAssociation.findPlayerPointsPerGame(players, 5);
                System.out.println(player);
                assert (player.equals(players[1]));

                player = PocuBasketballAssociation.findPlayerPointsPerGame(players, 7);
                System.out.println(player);
                assert (player.equals(players[2]));

                player = PocuBasketballAssociation.findPlayerPointsPerGame(players, 10);
                System.out.println(player);
                assert (player.equals(players[3]));

                player = PocuBasketballAssociation.findPlayerPointsPerGame(players, 11);
                System.out.println(player);
                assert (player.equals(players[4]));

                player = PocuBasketballAssociation.findPlayerPointsPerGame(players, 15);
                System.out.println(player);
                assert (player.equals(players[5]));

                player = PocuBasketballAssociation.findPlayerPointsPerGame(players, 16);
                System.out.println(player);
                assert (player.equals(players[6]));


                System.out.println("-------------------------------------------------ppppppp");
            }



            {
                Player[] players = new Player[] {
                        new Player("Player 1", 2, 5, 10, 78),
                        new Player("Player 2", 10, 4, 5, 66),
                        new Player("Player 3", 3, 3, 2, 22),
                        new Player("Player 4", 1, 9, 8, 12),
                        new Player("Player 5", 11, 1, 12, 26),
                        new Player("Player 6", 7, 2, 10, 15),
                        new Player("Player 7", 8, 15, 3, 11),
                        new Player("Player 8", 5, 7, 13, 5),
                        new Player("Player 9", 8, 2, 7, 67),
                        new Player("Player 10", 1, 11, 1, 29),
                        new Player("Player 11", 2, 6, 9, 88)
                };

                Player[] scratch = new Player[players.length];

                int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 6

                //assert (k == 6);
            }


            {
                Player[] players = new Player[] {
                        new Player("Player 1", 2, 5, 10, 78),
                    };

                Player[] scratch = new Player[players.length];

                int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 1

                assert (k == 1);
            }

            {
                Player[] players = new Player[] {
                        new Player("Player 1", 2, 5, 10, 78),
                        new Player("Player 2", 2, 0, 5, 0),
                };

                Player[] scratch = new Player[players.length];

                int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 1

                assert (k == 1);
            }



            {
                Player[] players = new Player[] {
                        new Player("Player 1", 2, 5, 10, 78),
                        new Player("Player 2", 10, 4, 5, 66),
                        new Player("Player 3", 3, 3, 2, 22),
                        new Player("Player 4", 1, 9, 8, 12),
                        new Player("Player 5", 11, 1, 12, 26),
                        new Player("Player 6", 7, 2, 10, 15),
                        new Player("Player 7", 8, 15, 3, 11),
                        new Player("Player 8", 5, 7, 13, 5),
                        new Player("Player 9", 8, 2, 7, 67),
                        new Player("Player 10", 1, 11, 1, 29),
                        new Player("Player 11", 2, 6, 9, 88)
                };

                Player[] scratch = new Player[players.length];

                int k = PocuBasketballAssociation.findDreamTeamSize(players, scratch); // k: 6
                //assert (k == 6);


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

                long maxTeamwork = PocuBasketballAssociation.findDreamTeam(players, k, outPlayers, scratch); // maxTeamwork: 171, outPlayers: [ Player 6, Player 5, Player 2, Player 7 ]

                assert (maxTeamwork == 171);
                for (int i = 0; i < outPlayers.length; ++i)
                {
                    System.out.println(outPlayers[i]);
                }


                System.out.println("---------------------------------------------------------------------");
            }


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

                long result = PocuBasketballAssociation.find3ManDreamTeam(players, outPlayers, scratch);

                for (int i = 0; i < outPlayers.length; ++i)
                {
                    System.out.println(outPlayers[i]);
                }

                assert (result == 219);
            }



            {
                Player[] players = new Player[] {
                        new Player("Player 4", 10, 10, 15, 25),
                        new Player("Player 2", 5, 2, 11, 31),
                        new Player("Player 3", 7, 4, 7, 44),
                        new Player("Player 1", 1, 5, 1, 60),
                        new Player("Player 6", 15, 0, 12, 61),
                        new Player("Player 7", 16, 8, 2, 70),
                        new Player("Player 5", 11, 12, 6, 77)
                };

                Player player = PocuBasketballAssociation.findPlayerShootingPercentage(players, 28); // player: Player 2
                assert (player.equals(players[1]));
                player = PocuBasketballAssociation.findPlayerShootingPercentage(players, 58); // player: Player 1
                assert (player.equals(players[3]));
                player = PocuBasketballAssociation.findPlayerShootingPercentage(players, 72); // player: Player 7
                assert (player.equals(players[5]));

                for (int i = 0; i <= 27; ++i)
                {
                    player = PocuBasketballAssociation.findPlayerShootingPercentage(players, i);
                    assert (player.equals(players[0]));
                }

                for (int i = 28; i <= 37; ++i)
                {
                    player = PocuBasketballAssociation.findPlayerShootingPercentage(players, i);
                    assert (player.equals(players[1]));
                }


                for (int i = 38; i <= 51; ++i)
                {
                    player = PocuBasketballAssociation.findPlayerShootingPercentage(players, i);
                    assert (player.equals(players[2]));
                }

                for (int i = 52; i <= 60; ++i)
                {
                    player = PocuBasketballAssociation.findPlayerShootingPercentage(players, i);
                    assert (player.equals(players[3]));
                }

                for (int i = 61; i <= 65; ++i)
                {
                    player = PocuBasketballAssociation.findPlayerShootingPercentage(players, i);
                    assert (player.equals(players[4]));
                }

                for (int i = 66; i <= 73; ++i)
                {
                    player = PocuBasketballAssociation.findPlayerShootingPercentage(players, i);
                    assert (player.equals(players[5]));
                }
                for (int i = 74; i <= 100; ++i)
                {
                    player = PocuBasketballAssociation.findPlayerShootingPercentage(players, i);
                    assert (player.equals(players[6]));
                }
            }



            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
                    new Player("Player 4", 10, 10, 15, 25),
                    new Player("Player 5", 11, 12, 6, 77),
                    new Player("Player 6", 15, 0, 12, 61),
                    new Player("Player 7", 16, 8, 2, 70),
                    new Player("Player 8", 19, 8, 2, 70),
                    new Player("Player 9", 20, 8, 2, 70),
                    new Player("Player 10", 21, 8, 2, 70),
                    new Player("Player 11", 33, 8, 2, 70),

            };

            Player[] expect = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
                    new Player("Player 4", 10, 10, 15, 25),
                    new Player("Player 5", 11, 12, 6, 77),
                    new Player("Player 6", 15, 0, 12, 61),
                    new Player("Player 7", 16, 8, 2, 70),
                    new Player("Player 8", 19, 8, 2, 70),
                    new Player("Player 9", 20, 8, 2, 70),
                    new Player("Player 10", 21, 8, 2, 70),
                    new Player("Player 11", 33, 8, 2, 70),
            };

            int[] target = {1, 5, 7, 10, 11, 15, 16, 19, 20, 21, 33};


            for (int i = 0; i < target.length; ++i)
            {
                assert(PocuBasketballAssociation.findPlayerPointsPerGame(players, target[i]).equals(expect[i]));
            }
        }

        {
            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
                    new Player("Player 4", 10, 10, 15, 25),
                    new Player("Player 5", 11, 12, 6, 77),
                    new Player("Player 6", 15, 0, 12, 61),
                    new Player("Player 7", 16, 8, 2, 70),
                    new Player("Player 8", 19, 8, 2, 70),
                    new Player("Player 9", 20, 8, 2, 70),
                    new Player("Player 10", 21, 8, 2, 70),
                    new Player("Player 11", 33, 8, 2, 70),
                    new Player("Player 12", 500, 8, 2, 70)
            };

            Player[] expect = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
                    new Player("Player 4", 10, 10, 15, 25),
                    new Player("Player 5", 11, 12, 6, 77),
                    new Player("Player 6", 15, 0, 12, 61),
                    new Player("Player 7", 16, 8, 2, 70),
                    new Player("Player 8", 19, 8, 2, 70),
                    new Player("Player 9", 20, 8, 2, 70),
                    new Player("Player 10", 21, 8, 2, 70),
                    new Player("Player 11", 33, 8, 2, 70),
                    new Player("Player 12", 500, 8, 2, 70)
            };

            int[] target = {1, 5, 7, 10, 11, 15, 16, 19, 20, 21, 33, 500};


            for (int i = 0; i < target.length; ++i)
            {
                assert(PocuBasketballAssociation.findPlayerPointsPerGame(players, target[i]).equals(expect[i]));
            }
        }


        {
            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
            };

            Player[] expect = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
            };

            int[] target = {1, 5, 7};


            for (int i = 0; i < target.length; ++i)
            {
                assert(PocuBasketballAssociation.findPlayerPointsPerGame(players, target[i]).equals(expect[i]));
            }
        }

        {
            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
            };

            Player[] expect = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
            };

            int[] target = {1, 5};


            for (int i = 0; i < target.length; ++i)
            {
                assert(PocuBasketballAssociation.findPlayerPointsPerGame(players, target[i]).equals(expect[i]));
            }
        }

        {
            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
            };

            Player[] expect = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
            };

            int[] target = {1};


            for (int i = 0; i < target.length; ++i)
            {
                assert(PocuBasketballAssociation.findPlayerPointsPerGame(players, target[i]).equals(expect[i]));
            }
        }

        {
            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
                    new Player("Player 4", 10, 10, 15, 25),
                    new Player("Player 5", 19, 12, 6, 77),
                    new Player("Player 6", 23, 0, 12, 61),
                    new Player("Player 7", 36, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
            };

            Player[] expect = new Player[] {
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),

            };

            int[] target = {45, 46, 47, 48, 44};

            for (int i = 0; i < target.length; ++i)
            {
                assert(PocuBasketballAssociation.findPlayerPointsPerGame(players, target[i]).equals(expect[i]));
            }

        }

        {
            Player[] players = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
                    new Player("Player 4", 9, 10, 15, 25),
                    new Player("Player 5", 10, 12, 6, 77),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
            };

            Player[] expect = new Player[] {
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 1", 1, 5, 1, 60),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 2", 5, 2, 11, 31),
                    new Player("Player 3", 7, 4, 7, 44),
                    new Player("Player 3", 7, 4, 7, 44),
                    new Player("Player 4", 9, 10, 15, 25),
                    new Player("Player 4", 9, 10, 15, 25),
                    new Player("Player 5", 10, 12, 6, 77),
                    new Player("Player 5", 10, 12, 6, 77),
                    new Player("Player 5", 10, 12, 6, 77),
                    new Player("Player 5", 10, 12, 6, 77),
                    new Player("Player 5", 10, 12, 6, 77),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 6", 20, 0, 12, 61),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 7", 30, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 8", 40, 8, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 9", 50, 50, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 10", 60, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
                    new Player("Player 11", 70, 8, 2, 70),
            };

            for (int i = 0; i < 100; ++i)
            {
                assert(PocuBasketballAssociation.findPlayerPointsPerGame(players, i).equals(expect[i]));
            }

        }
    }
}
