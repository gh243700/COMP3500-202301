package academy.pocu.comp3500.assignment3;

public class Bitmap {
    private long[] board = new long[12];
    private static final char[] LETTERS = {'k', 'r', 'b', 'q', 'n', 'p', 'K', 'R', 'B', 'Q', 'N', 'P'};
    private static final int[] VALUES = {900, 50, 30, 90, 30, 10, 900, 50, 30, 90, 30, 10};

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

        long mask = 0x01;
        for (int i = 0; i < 64; ++i) {
            for (int k = 0; k < 12; ++k) {
                if ((board[k] & mask) != 0) {
                    if (k < 6) {
                        whiteScore += VALUES[k];
                    } else {
                        blackScore += VALUES[k];
                    }
                }
            }

            mask = mask << 1;
        }

        return whiteScore - blackScore;
    }


    public static Bitmap convertToBitmap(char[][] board) {
        Bitmap bitmap = new Bitmap();

        for (int i = 0; i < 64; ++i) {
            char c = board[i / 8][i % 8];
            switch (c) {
                case 'k':
                    bitmap.on(i, ChessPieceType.WHITE_KING);
                    break;
                case 'r':
                    bitmap.on(i, ChessPieceType.WHITE_ROOK);
                    break;
                case 'b':
                    bitmap.on(i, ChessPieceType.WHITE_BISHOP);
                    break;
                case 'q':
                    bitmap.on(i, ChessPieceType.WHITE_QUEEN);
                    break;
                case 'n':
                    bitmap.on(i, ChessPieceType.WHITE_KNIGHT);
                    break;
                case 'p':
                    bitmap.on(i, ChessPieceType.WHITE_PAWN);
                    break;
                case 'K':
                    bitmap.on(i, ChessPieceType.BLACK_KING);
                    break;
                case 'R':
                    bitmap.on(i, ChessPieceType.BLACK_ROOK);
                    break;
                case 'B':
                    bitmap.on(i, ChessPieceType.BLACK_BISHOP);
                    break;
                case 'Q':
                    bitmap.on(i, ChessPieceType.BLACK_QUEEN);
                    break;
                case 'N':
                    bitmap.on(i, ChessPieceType.BLACK_KNIGHT);
                    break;
                case 'P':
                    bitmap.on(i, ChessPieceType.BLACK_PAWN);
                    break;
                default:
                    break;
            }
        }

        return bitmap;
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
