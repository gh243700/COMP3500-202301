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
            return front;
        }

        int mid = (front + back) / 2;

        if (mid + 1 >= players.length) {
            return Math.abs(players[mid - 1].getPointsPerGame() - target) > Math.abs(players[mid].getPointsPerGame() - target) ? mid : mid - 1;
        }

        if (mid - 1 < 0) {
            return Math.abs(players[mid + 1].getPointsPerGame() - target) > Math.abs(players[mid].getPointsPerGame() - target) ? mid : mid + 1;
        }

        int absLeft = Math.abs(target - players[mid - 1].getPointsPerGame());
        int absRight = Math.abs(target - players[mid + 1].getPointsPerGame());
        int absMid = Math.abs(target - players[mid].getPointsPerGame());

        if (absMid <= absLeft && absMid <= absRight
                && players[mid].getPointsPerGame() != players[mid + 1].getPointsPerGame()
                && players[mid].getPointsPerGame() != players[mid - 1].getPointsPerGame()) {
            if (absMid == absRight) {
                return mid + 1;
            }

            return mid;
        }

        if (players[mid].getPointsPerGame() > target) {
            return findPlayerCloseToPoint(players, front, mid - 1, target);
        }
        return findPlayerCloseToPoint(players, mid + 1, back, target);
    }

    public static Player findPlayerPointsPerGame(final Player[] players, int targetPoints) {
        final int index = findPlayerCloseToPoint(players, 0, players.length - 1, targetPoints);
        return players[index];
    }

    public static int findPlayerCloseToShootingPercentage(final Player[] players, final int front, final int back, final int shootingPercentage) {
        if (front >= back) {
            return front;
        }

        final int mid = (front + back) / 2;

        if (mid - 1 < 0) {
            return Math.abs(shootingPercentage - players[mid].getShootingPercentage())
                    > Math.abs(shootingPercentage - players[mid + 1].getShootingPercentage())
                    ? mid + 1 : mid;
        }

        if (mid + 1 >= players.length) {
            return Math.abs(shootingPercentage - players[mid].getShootingPercentage()) >
                    Math.abs(shootingPercentage - players[mid - 1].getShootingPercentage())
                    ? mid - 1 : mid;
        }

        final int leftAbs = Math.abs(shootingPercentage - players[mid - 1].getShootingPercentage());
        final int rightAbs = Math.abs(shootingPercentage - players[mid + 1].getShootingPercentage());
        final int midAbs = Math.abs(shootingPercentage - players[mid].getShootingPercentage());

        if (leftAbs >= midAbs && rightAbs >= midAbs
                && players[mid].getShootingPercentage() != players[mid + 1].getShootingPercentage()
                && players[mid].getShootingPercentage() != players[mid - 1].getShootingPercentage()) {
            if (midAbs == rightAbs) {
                return mid + 1;
            }
            return mid;
        }

        if (shootingPercentage < players[mid].getShootingPercentage()) {
            return findPlayerCloseToShootingPercentage(players, front, mid - 1, shootingPercentage);
        }

        return findPlayerCloseToShootingPercentage(players, mid + 1, back, shootingPercentage);
    }

    public static Player findPlayerShootingPercentage(final Player[] players, int targetShootingPercentage) {

        final int result = findPlayerCloseToShootingPercentage(players, 0, players.length, targetShootingPercentage);

        return players[result];
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

    public static long find3ManDreamTeam(final Player[] players, final Player[] outPlayers, final Player[] scratch) {
        sortPlayersByPassPerGame(players, 0, players.length - 1);

        scratch[0] = players[0];
        scratch[1] = players[1];

        long maxTeamwork = 0;

        for (int i = 2; i < players.length; ++i) {
            long passSum = scratch[0].getPassesPerGame() + scratch[1].getPassesPerGame() + players[i].getPassesPerGame();
            long minAssist = scratch[0].getAssistsPerGame() < scratch[1].getAssistsPerGame() ?
                    scratch[0].getAssistsPerGame() : scratch[1].getAssistsPerGame();
            minAssist = minAssist < players[i].getAssistsPerGame() ? minAssist : players[i].getAssistsPerGame();
            long result = passSum * minAssist;
            if (maxTeamwork < result) {
                maxTeamwork = result;
                scratch[2] = players[i];
            }
        }

        outPlayers[0] = scratch[0];
        outPlayers[1] = scratch[1];
        outPlayers[2] = scratch[2];

        return maxTeamwork;
    }

    public static long findDreamTeam(final Player[] players, int k, final Player[] outPlayers, final Player[] scratch) {
        sortPlayersByPassPerGame(players, 0, players.length - 1);
        long passSum = 0;
        long minAssist = players[0].getAssistsPerGame();
        for (int i = 0; i < k - 1; ++i) {
            scratch[i] = players[i];
            outPlayers[i] = players[i];
            passSum += scratch[i].getPassesPerGame();
            if (minAssist > players[i].getAssistsPerGame()) {
                minAssist = players[i].getAssistsPerGame();
            }
        }

        long maxTeamwork = 0;

        for (int i = scratch.length - 1; i < players.length; ++i) {
            long tempSum = passSum + players[i].getPassesPerGame();
            long tempMin = minAssist < players[i].getAssistsPerGame() ? minAssist : players[i].getAssistsPerGame();
            long result = tempSum * tempMin;
            if (maxTeamwork < result) {
                maxTeamwork = result;
                scratch[k - 1] = players[i];
            }
        }

        outPlayers[k - 1] = scratch[k - 1];

        return maxTeamwork;
    }

    public static int findDreamTeamSize(final Player[] players, final Player[] scratch) {

        if (players.length <= 1) {
            return players.length;
        }

        long sumOfPassPoints = 0;

        for (int i = 0; i < players.length; ++i) {
            for (int k = 0; k < players.length - 1; ++k) {
                if (players[players.length - k - 1].getPassesPerGame() > players[players.length - k - 2].getPassesPerGame()) {
                    Player temp = players[players.length - k - 2];
                    players[players.length - k - 2] = players[players.length - k - 1];
                    players[players.length - k - 1] = temp;
                }
            }
            sumOfPassPoints += players[i].getPassesPerGame();
        }

        long averageOfSumPoints = sumOfPassPoints / players.length;

        int maxLoop = 0;

        for (int i = 0; i < players.length; ++i) {
            if (players[i].getPassesPerGame() - averageOfSumPoints >= 0) {
                ++maxLoop;
                scratch[i] = players[i];
            }
        }

        long maxTeamwork = 0;
        sumOfPassPoints = 0;
        int minAssist = Integer.MAX_VALUE;
        int bestTeamNumber = -1;

        for (int i = 0; i < maxLoop; ++i) {
            long tempMaxTeamWork = maxTeamwork;
            for (int k = 0; k < i - 1; ++k) {
                sumOfPassPoints += players[k].getPassesPerGame();

                if (players[k].getAssistsPerGame() < minAssist) {
                    minAssist = players[k].getAssistsPerGame();
                }
            }

            for (int k = i; k < players.length; ++k) {
                int tempMinAssist = minAssist;

                if (players[k].getAssistsPerGame() < tempMinAssist) {
                    tempMinAssist = players[k].getAssistsPerGame();
                }

                long tempPassSum = players[k].getPassesPerGame() + sumOfPassPoints;

                long teamWork = tempPassSum * tempMinAssist;

                if (tempMaxTeamWork < teamWork) {
                    tempMaxTeamWork = teamWork;
                }
            }

            if (maxTeamwork < tempMaxTeamWork) {
                maxTeamwork = tempMaxTeamWork;
                bestTeamNumber = i;
            }

            minAssist = Integer.MAX_VALUE;
        }

        return bestTeamNumber + 1;
    }
}