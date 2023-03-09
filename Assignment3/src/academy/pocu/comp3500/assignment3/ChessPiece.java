package academy.pocu.comp3500.assignment3;

public class ChessPiece {
    private ChessPieceType type;
    private int offset;

    public ChessPiece(ChessPieceType type, int offset) {
        this.type = type;
        this.offset = offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public ChessPieceType getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }
}
