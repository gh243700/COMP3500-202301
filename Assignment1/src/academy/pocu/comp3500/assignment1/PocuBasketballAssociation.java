package academy.pocu.comp3500.assignment1;

import academy.pocu.comp3500.assignment1.pba.Player;
import academy.pocu.comp3500.assignment1.pba.GameStat;

public final class PocuBasketballAssociation {
    private PocuBasketballAssociation() {
    }

    public static void quickSortGameStat(final GameStat[] gameStat, final int leftIndex, final int rightIndex) {
        if (leftIndex >= rightIndex) {
            return;
        }

        int p = (leftIndex - 1);
        final int pivotHash = gameStat[rightIndex].getPlayerName().hashCode();
        for (int i = leftIndex; i < rightIndex; ++i) {
            if (gameStat[i].getPlayerName().hashCode() < pivotHash) {
                ++p;
                GameStat temp = gameStat[p];
                gameStat[p] = gameStat[i];
                gameStat[i] = temp;
            }
        }

        ++p;
        GameStat temp = gameStat[p];
        gameStat[p] = gameStat[rightIndex];
        gameStat[rightIndex] = temp;

        quickSortGameStat(gameStat, leftIndex, p - 1);
        quickSortGameStat(gameStat, p + 1, rightIndex);
    }

    public static void processGameStats(final GameStat[] gameStats, final Player[] outPlayers) {

        quickSortGameStat(gameStats, 0, gameStats.length - 1);
        assert (gameStats.length >= 1);

        int outPlayersIndex = 0;
        int previousHash = gameStats[0].getPlayerName().hashCode();

        int points = gameStats[0].getPoints();
        int assists = gameStats[0].getAssists();
        int passes = gameStats[0].getNumPasses();
        int goalAttempts = gameStats[0].getGoalAttempts();
        int goal = gameStats[0].getGoals();

        int gamesPlayed = 1;
        for (int i = 1; i < gameStats.length + 1; ++i) {
            if (i >= gameStats.length || previousHash != gameStats[i].getPlayerName().hashCode()) {
                Player player = outPlayers[outPlayersIndex];
                player.setName(gameStats[i - 1].getPlayerName());
                player.setPointsPerGame(points / gamesPlayed);
                player.setAssistsPerGame(assists / gamesPlayed);
                player.setPassesPerGame(passes / gamesPlayed);
                player.setShootingPercentage((int) (100.0 * goal / goalAttempts));

                if (i >= gameStats.length) {
                    break;
                }

                ++outPlayersIndex;

                gamesPlayed = 0;
                points = 0;
                assists = 0;
                passes = 0;
                goalAttempts = 0;
                goal = 0;

                previousHash = gameStats[i].getPlayerName().hashCode();
            }

            points += gameStats[i].getPoints();
            assists += gameStats[i].getAssists();
            passes += gameStats[i].getNumPasses();
            goalAttempts += gameStats[i].getGoalAttempts();
            goal += gameStats[i].getGoals();

            gamesPlayed++;
        }
    }

    public static int findPlayerCloseToPoint(final Player[] players, final int front, final int back, final int target) {

        if (front >= back) {
            int index = front;

            if (index - 1 >= 0 && Math.abs(players[index - 1].getPointsPerGame() - target) < Math.abs(players[index].getPointsPerGame() - target)) {
                return index - 1;
            }

            while (index + 1 < players.length && players[index].getPointsPerGame() == players[index + 1].getPointsPerGame()) {
                ++index;
            }

            return index;
        }

        int mid = (front + back) / 2;

        long midPoint = players[mid].getPointsPerGame();

        if (target > midPoint) {
            return findPlayerCloseToPoint(players, mid + 1, back, target);
        }

        return findPlayerCloseToPoint(players, front, mid, target);
    }

    public static Player findPlayerPointsPerGame(final Player[] players, int targetPoints) {
        final int index = findPlayerCloseToPoint(players, 0, players.length - 1, targetPoints);
        return players[index];
    }

    public static int findPlayerCloseToShootingPercentage(final Player[] players, final int front, final int back, final int shootingPercentage) {
        if (front >= back) {
            int index = front;

            if (index - 1 >= 0 && Math.abs(players[index - 1].getShootingPercentage() - shootingPercentage) < Math.abs(players[index].getShootingPercentage() - shootingPercentage)) {
                return index - 1;
            }

            while (index + 1 < players.length && players[index].getShootingPercentage() == players[index + 1].getShootingPercentage()) {
                ++index;
            }

            return index;
        }

        final int mid = (front + back) / 2;

        if (shootingPercentage > players[mid].getShootingPercentage()) {
            return findPlayerCloseToShootingPercentage(players, mid + 1, back, shootingPercentage);
        }

        return findPlayerCloseToShootingPercentage(players, front, mid, shootingPercentage);
    }

    public static Player findPlayerShootingPercentage(final Player[] players, int targetShootingPercentage) {

        final int result = findPlayerCloseToShootingPercentage(players, 0, players.length - 1, targetShootingPercentage);

        return players[result];
    }

    public static void sortPlayersByAssistsPerGame(final Player[] players, final int front, final int back) {
        if (front >= back) {
            return;
        }

        int left = (front - 1);
        for (int i = front; i < back; ++i) {
            if (players[i].getAssistsPerGame() > players[back].getAssistsPerGame()) {
                ++left;
                Player temp = players[i];
                players[i] = players[left];
                players[left] = temp;
            }
        }

        ++left;
        Player temp = players[left];
        players[left] = players[back];
        players[back] = temp;

        sortPlayersByAssistsPerGame(players, front, left - 1);
        sortPlayersByAssistsPerGame(players, left + 1, back);
    }

    public static long find3ManDreamTeam(final Player[] players, final Player[] outPlayers, final Player[] scratch) {
        sortPlayersByAssistsPerGame(players, 0, players.length - 1);


        if (players[0].getPassesPerGame() > players[1].getPassesPerGame()) {
            scratch[0] = players[0];
            scratch[1] = players[1];
        } else {
            scratch[0] = players[1];
            scratch[1] = players[0];
        }

        scratch[2] = players[2];

        outPlayers[0] = scratch[0];
        outPlayers[1] = scratch[1];
        outPlayers[2] = scratch[2];


        long maxTeamwork = (scratch[0].getPassesPerGame() + scratch[1].getPassesPerGame() + scratch[2].getPassesPerGame()) * scratch[2].getAssistsPerGame();

        for (int k = 3; k < players.length; ++k) {
            scratch[2] = players[k];
            if (scratch[0].getPassesPerGame() < players[k - 1].getPassesPerGame()) {
                scratch[1] = scratch[0];
                scratch[0] = players[k - 1];
            } else if (scratch[1].getPassesPerGame() < players[k - 1].getPassesPerGame()) {
                scratch[1] = players[k - 1];
            }

            long tempTeamWork = (scratch[0].getPassesPerGame() + scratch[1].getPassesPerGame() + scratch[2].getPassesPerGame()) * scratch[2].getAssistsPerGame();

            if (tempTeamWork > maxTeamwork) {
                maxTeamwork = tempTeamWork;

                outPlayers[0] = scratch[0];
                outPlayers[1] = scratch[1];
                outPlayers[2] = scratch[2];
            }
        }

        return maxTeamwork;
    }

    public static void sortPlayersByPassPerGame(final Player[] players, final int front, final int back) {
        if (front >= back) {
            return;
        }

        int left = (front - 1);
        for (int i = front; i < back; ++i) {
            if (players[i].getPassesPerGame() > players[back].getPassesPerGame()) {
                ++left;
                Player temp = players[i];
                players[i] = players[left];
                players[left] = temp;
            }
        }

        ++left;
        Player temp = players[left];
        players[left] = players[back];
        players[back] = temp;

        sortPlayersByPassPerGame(players, front, left - 1);
        sortPlayersByPassPerGame(players, left + 1, back);
    }

    public static long findDreamTeam(final Player[] players, int k, final Player[] outPlayers, final Player[] scratch) {
        sortPlayersByAssistsPerGame(players, 0, players.length - 1);

        long passSum = 0;
        for (int i = 0; i < scratch.length - 1; ++i) {
            scratch[i] = players[i];
            passSum += players[i].getPassesPerGame();
        }

        sortPlayersByPassPerGame(scratch, 0, scratch.length - 2);

        scratch[k - 1] = players[k - 1];
        passSum += scratch[k - 1].getPassesPerGame();

        for (int i = 0; i < scratch.length; ++i) {
            outPlayers[i] = scratch[i];
        }

        long maxTeamwork = passSum * scratch[k - 1].getAssistsPerGame();

        for (int i = k; i < players.length; ++i) {
            scratch[k - 1] = players[i];

            Player player = players[i - 1];
            passSum = 0;

            for (int j = 0; j < scratch.length - 1; ++j) {
                if (scratch[j].getPassesPerGame() < player.getPassesPerGame()) {
                    Player temp = scratch[j];
                    scratch[j] = player;
                    player = temp;
                }

                passSum += scratch[j].getPassesPerGame();
            }

            passSum += scratch[k - 1].getPassesPerGame();

            long tempTeamWork = passSum * scratch[k - 1].getAssistsPerGame();

            if (tempTeamWork > maxTeamwork) {
                maxTeamwork = tempTeamWork;

                for (int j = 0; j < scratch.length; ++j) {
                    outPlayers[j] = scratch[j];
                }
            }
        }

        return maxTeamwork;
    }

    public static int findDreamTeamSize(final Player[] players, final Player[] scratch) {

        sortPlayersByAssistsPerGame(players, 0, players.length - 1);

        long maxTeamwork = 0;
        int bestTeamSize = 0;

        for (int i = 0; i < scratch.length; ++i) {
            scratch[i] = players[i];
        }

        sortPlayersByPassPerGame(scratch, 0, scratch.length - 1);

        for (int i = 0; i < players.length; ++i) {
            Player useOfAssist = players[i];
            long sumOfPasses = 0;
            int playersNeeded = i;
            int scratchIndex = 0;

            boolean fail = false;

            while (playersNeeded > 0 && scratchIndex < scratch.length) {
                if (players[i] != scratch[scratchIndex] && scratch[scratchIndex].getAssistsPerGame() > useOfAssist.getAssistsPerGame()) {
                    sumOfPasses += scratch[scratchIndex].getPassesPerGame();
                    --playersNeeded;
                }

                ++scratchIndex;

                if (scratchIndex >= scratch.length && playersNeeded > 0) {
                    if (i + 1 >= players.length) {
                        fail = true;
                    }

                    sumOfPasses = 0;
                    useOfAssist = players[i + 1];
                    scratchIndex = 0;
                    playersNeeded = i;
                }
            }

            if (fail) {
                continue;
            }

            sumOfPasses += useOfAssist.getPassesPerGame();

            long tempTeamWork = sumOfPasses * useOfAssist.getAssistsPerGame();

            if (maxTeamwork < tempTeamWork) {
                maxTeamwork = tempTeamWork;
                bestTeamSize = i + 1;
            }
        }

        return bestTeamSize;
    }
}