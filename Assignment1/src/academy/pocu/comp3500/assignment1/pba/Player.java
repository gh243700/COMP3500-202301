package academy.pocu.comp3500.assignment1.pba;

import java.util.Objects;

public final class Player {
    private String name;
    private int pointsPerGame;
    private int assistsPerGame;
    private int passesPerGame;
    private int shootingPercentage;

    public Player() { }

    public Player(final String name,
                  final int pointsPerGame,
                  final int assistsPerGame,
                  final int passesPerGame,
                  final int shootingPercentage) {
        this.name = name;
        this.pointsPerGame = pointsPerGame;
        this.assistsPerGame = assistsPerGame;
        this.passesPerGame = passesPerGame;
        this.shootingPercentage = shootingPercentage;
    }

    public int getPointsPerGame() {
        return this.pointsPerGame;
    }

    public void setPointsPerGame(int pointsPerGame) {
        this.pointsPerGame = pointsPerGame;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAssistsPerGame() {
        return this.assistsPerGame;
    }

    public void setAssistsPerGame(int assistsPerGame) {
        this.assistsPerGame = assistsPerGame;
    }

    public int getPassesPerGame() {
        return this.passesPerGame;
    }

    public void setPassesPerGame(int passesPerGame) {
        this.passesPerGame = passesPerGame;
    }

    public int getShootingPercentage() {
        return this.shootingPercentage;
    }

    public void setShootingPercentage(int shootingPercentage) {
        this.shootingPercentage = shootingPercentage;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", pointsPerGame=" + pointsPerGame +
                ", assistsPerGame=" + assistsPerGame +
                ", passesPerGame=" + passesPerGame +
                ", shootingPercentage=" + shootingPercentage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return pointsPerGame == player.pointsPerGame && assistsPerGame == player.assistsPerGame && passesPerGame == player.passesPerGame && shootingPercentage == player.shootingPercentage && name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pointsPerGame, assistsPerGame, passesPerGame, shootingPercentage);
    }
}
