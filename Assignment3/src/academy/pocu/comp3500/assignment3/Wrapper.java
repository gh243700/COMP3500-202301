package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;

public class Wrapper {
    private int eval;
    private Move move;

    public Wrapper(int eval, Move move) {
        this.move = move;
        this.eval = eval;
    }

    public Wrapper() {
    }

    public void reset(int eval, Move move) {
        this.eval = eval;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    public int getEval() {
        return eval;
    }
}
