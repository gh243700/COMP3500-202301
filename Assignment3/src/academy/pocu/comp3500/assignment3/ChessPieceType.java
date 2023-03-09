package academy.pocu.comp3500.assignment3;

public enum ChessPieceType {
    WHITE_KING,
    WHITE_ROOK,
    WHITE_BISHOP,
    WHITE_QUEEN,
    WHITE_KNIGHT,
    WHITE_PAWN,
    BLACK_KING,
    BLACK_ROOK,
    BLACK_BISHOP,
    BLACK_QUEEN,
    BLACK_KNIGHT,
    BLACK_PAWN,
    NONE;

    public static ChessPieceType fromInteger(int x) {
        ChessPieceType chessPieceType;
        switch (x) {
            case 0:
                chessPieceType = WHITE_KING;
                break;
            case 1:
                chessPieceType = WHITE_ROOK;
                break;
            case 2:
                chessPieceType = WHITE_BISHOP;
                break;
            case 3:
                chessPieceType = WHITE_QUEEN;
                break;
            case 4:
                chessPieceType = WHITE_KNIGHT;
                break;
            case 5:
                chessPieceType = WHITE_PAWN;
                break;
            case 6:
                chessPieceType = BLACK_KING;
                break;
            case 7:
                chessPieceType = BLACK_ROOK;
                break;
            case 8:
                chessPieceType = BLACK_BISHOP;
                break;
            case 9:
                chessPieceType = BLACK_QUEEN;
                break;
            case 10:
                chessPieceType = BLACK_KNIGHT;
                break;
            case 11:
                chessPieceType = BLACK_PAWN;
                break;
            default:
                chessPieceType = NONE;
                break;
        }
        return chessPieceType;
    }
}
