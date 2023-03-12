package academy.pocu.comp3500.assignment3;
import academy.pocu.comp3500.assignment3.chess.Move;

public class Evaluation {
    public int score;
    public Move move;

    public Evaluation(int score, Move move) {
        this.score = score;
        this.move = move;
    }
}