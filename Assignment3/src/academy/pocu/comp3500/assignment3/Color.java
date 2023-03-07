package academy.pocu.comp3500.assignment3;

public enum Color {
    WHITE,
    BLACK,
    NONE;

    public static Color chessPieceColor(ChessPieceType chessPieceType) {
        if (chessPieceType == ChessPieceType.NONE) {
            return Color.NONE;
        }

        return (ChessPieceType.BLACK_KING.ordinal() > chessPieceType.ordinal()) ? Color.WHITE : Color.BLACK;
    }
}
