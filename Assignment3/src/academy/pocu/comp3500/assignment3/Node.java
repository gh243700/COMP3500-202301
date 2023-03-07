package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;

import java.util.ArrayList;

public class Node {
    private Bitmap bitmap;
    private Move move;
    private Node parent;
    private int evaluationValue;


    public Node() {
    }

    public Node(Bitmap bitmap, Move move, Node parent) {
        this.bitmap = bitmap;
        this.move = move;
        this.parent = parent;
    }

    public void setEvaluationValue(int evaluationValue) {
        this.evaluationValue = evaluationValue;
    }

    public int getEvaluationValue() {
        return evaluationValue;
    }

    public void reset(Bitmap bitmap, Move move, Node parent) {
        this.bitmap = bitmap;
        this.move = move;
        this.parent = parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Move getMove() {
        return move;
    }

}
