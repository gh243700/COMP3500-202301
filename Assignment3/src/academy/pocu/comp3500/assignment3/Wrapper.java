package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;

public class Wrapper {
    public int eval;
    public Move move;

    int from;

    public Wrapper(int eval, Move move) {
        this.move = move;
        this.eval = eval;
    }


    public Wrapper(int eval, Move move, int from) {
        this.move = move;
        this.eval = eval;
        this.from = from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getFrom() {
        return from;
    }
}
