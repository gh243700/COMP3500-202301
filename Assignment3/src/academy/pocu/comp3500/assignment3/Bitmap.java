package academy.pocu.comp3500.assignment3;

public class Bitmap {
    private long[] board = new long[12];
    private static final char[] LETTERS = {'k', 'r', 'b', 'q', 'n', 'p', 'K', 'R', 'B', 'Q', 'N', 'P'};
    private static final int[] VALUES = {200, 5, 3, 9, 3, 1, 200, 5, 3, 9, 3, 1};
    private static int WHITE_KING_COUNT = 0;
    private static int WHITE_ROOK_COUNT = 0;
    private static int WHITE_BISHOP_COUNT = 0;
    private static int WHITE_QUEEN_COUNT = 0;
    private static int WHITE_KNIGHT_COUNT = 0;
    private static int WHITE_PAWN_COUNT = 0;
    private static int BLACK_KING_COUNT = 0;
    private static int BLACK_ROOK_COUNT = 0;
    private static int BLACK_BISHOP_COUNT = 0;
    private static int BLACK_QUEEN_COUNT = 0;
    private static int BLACK_KNIGHT_COUNT = 0;
    private static int BLACK_PAWN_COUNT = 0;
    private static int[] COUNTS = {
            WHITE_KING_COUNT,
            WHITE_ROOK_COUNT,
            WHITE_BISHOP_COUNT,
            WHITE_QUEEN_COUNT,
            WHITE_KNIGHT_COUNT,
            WHITE_PAWN_COUNT,
            BLACK_KING_COUNT,
            BLACK_ROOK_COUNT,
            BLACK_BISHOP_COUNT,
            BLACK_QUEEN_COUNT,
            BLACK_KNIGHT_COUNT,
            BLACK_PAWN_COUNT
    };

    public static void decreesCount(ChessPieceType type) {
        --COUNTS[type.ordinal()];
    }

    public static void increaseCount(ChessPieceType type) {
        ++COUNTS[type.ordinal()];
    }

    public ChessPieceType getChessPieceType(final int offset) {
        long mask = 0x1;
        mask = mask << offset;

        for (int i = 0; i < board.length; ++i) {
            if ((board[i] & mask) != 0) {
                return ChessPieceType.fromInteger(i);
            }
        }

        return ChessPieceType.NONE;
    }

    public Color chessPieceColor(final int offset) {
        long mask = 0x01;
        mask = mask << offset;

        for (int i = 0; i < board.length; ++i) {
            if ((board[i] & mask) != 0) {
                return (i < 6) ? Color.WHITE : Color.BLACK;
            }
        }

        return Color.NONE;
    }

    public void on(final int offset, final ChessPieceType type) {
        if (type == ChessPieceType.NONE) {
            return;
        }

        long mask = 0x01;
        mask = mask << offset;

        board[type.ordinal()] = board[type.ordinal()] | mask;
    }

    public void off(final int offset, final ChessPieceType type) {
        if (type == ChessPieceType.NONE) {
            return;
        }

        long mask = 0x01;
        mask = mask << offset;
        mask = ~mask;

        board[type.ordinal()] = board[type.ordinal()] & mask;
    }

    public Bitmap makeCopy() {
        Bitmap copy = new Bitmap();

        for (int i = 0; i < board.length; ++i) {
            copy.board[i] = board[i];
        }

        return copy;
    }

    public boolean GameOver() {

        long mask = 0x0;
        int white = 0;
        int black = 0;

        for (int k = 0; k < 6; ++k) {
            if ((board[k] | mask) != 0) {
                ++white;
                break;
            }
        }

        for (int k = 6; k < 12; ++k) {
            if ((board[k] | mask) != 0) {
                ++black;
                break;
            }

        }

        return white == 0 || black == 0;
    }

    public int evaluate() {
        int whiteScore = 0;
        int blackScore = 0;

        for (int i = 0; i < 12; ++i) {
            if (i < 6) {
                whiteScore += COUNTS[i] * VALUES[i];
            } else {
                blackScore += COUNTS[i] * VALUES[i];
            }
        }

        return whiteScore - blackScore;
    }


    public static void convertToBitmap(char[][] board, Bitmap bitmap) {

        for (int i = 0; i < 12; ++i) {
            bitmap.board[i] = bitmap.board[i] & 0x0;
        }

        for (int i = 0; i < 64; ++i) {
            char c = board[i / 8][i % 8];
            switch (c) {
                case 'k':
                    ++WHITE_KING_COUNT;
                    bitmap.on(i, ChessPieceType.WHITE_KING);
                    break;
                case 'r':
                    ++WHITE_ROOK_COUNT;
                    bitmap.on(i, ChessPieceType.WHITE_ROOK);
                    break;
                case 'b':
                    ++WHITE_BISHOP_COUNT;
                    bitmap.on(i, ChessPieceType.WHITE_BISHOP);
                    break;
                case 'q':
                    ++WHITE_QUEEN_COUNT;
                    bitmap.on(i, ChessPieceType.WHITE_QUEEN);
                    break;
                case 'n':
                    ++WHITE_KNIGHT_COUNT;
                    bitmap.on(i, ChessPieceType.WHITE_KNIGHT);
                    break;
                case 'p':
                    ++WHITE_PAWN_COUNT;
                    bitmap.on(i, ChessPieceType.WHITE_PAWN);
                    break;
                case 'K':
                    ++BLACK_KING_COUNT;
                    bitmap.on(i, ChessPieceType.BLACK_KING);
                    break;
                case 'R':
                    ++BLACK_ROOK_COUNT;
                    bitmap.on(i, ChessPieceType.BLACK_ROOK);
                    break;
                case 'B':
                    ++BLACK_BISHOP_COUNT;
                    bitmap.on(i, ChessPieceType.BLACK_BISHOP);
                    break;
                case 'Q':
                    ++BLACK_QUEEN_COUNT;
                    bitmap.on(i, ChessPieceType.BLACK_QUEEN);
                    break;
                case 'N':
                    ++BLACK_KING_COUNT;
                    bitmap.on(i, ChessPieceType.BLACK_KNIGHT);
                    break;
                case 'P':
                    ++BLACK_PAWN_COUNT;
                    bitmap.on(i, ChessPieceType.BLACK_PAWN);
                    break;
                default:
                    break;
            }
        }
    }

    public static char[][] convertToCharArray(Bitmap bitmap) {
        char[][] result = new char[8][8];

        long mask = 0x01;

        for (int i = 0; i < 64; ++i) {
            char c = 0;
            for (int k = 0; k < 12; ++k) {
                if ((bitmap.board[k] & mask) != 0) {
                    c = LETTERS[k];
                    break;
                }
            }

            result[i / 8][i % 8] = c;

            mask = mask << 1;
        }

        return result;
    }
}
