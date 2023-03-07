package academy.pocu.comp3500.assignment3;

public class Bitmap {
    private long[] board = new long[12];
    private static final char[] LETTERS = {'k', 'r', 'b', 'q', 'n', 'p', 'K', 'R', 'B', 'Q', 'N', 'P'};
    private static final int[] VALUES = {200, 5, 3, 9, 3, 1, -200, -5, -3, -9, -3, -1};

    public ChessPieceType getChessPieceType(final int offset) {
        long mask = 0x01;
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

    public void setBitmap(final int offset, final ChessPieceType type) {
        if (offset < 0 || offset >= 64) {
            return;
        }

        long mask = 0x01;
        mask = mask << offset;

        for (int i = 0; i < board.length; ++i) {
            if (i == type.ordinal()) {
                continue;
            }

            if ((board[i] & mask) != 0) {
                board[i] ^= mask;
                break;
            }
        }

        if (type == ChessPieceType.NONE) {
            return;
        }

        board[type.ordinal()] |= mask;
    }

    public Bitmap makeCopy() {
        Bitmap copy = new Bitmap();

        for (int i = 0; i < board.length; ++i) {
            copy.board[i] = board[i];
        }

        return copy;
    }

    public boolean isKingAlive(boolean isWhite) {
        return ((board[isWhite ? 0 : 6] | 0x0) != 0) ? true : false;
    }

    public int evaluate() {
        long mask = 0x01;
        int result = 0;
        for (int i = 0; i < 64; ++i) {
            for (int k = 0; k < board.length; ++k) {
                if ((board[k] & mask) != 0) {
                    result += VALUES[k];
                    break;
                }
            }

            mask = mask << 1;
        }

        return result;
    }


    public static Bitmap convertToBitmap(char[][] board) {
        Bitmap bitmap = new Bitmap();

        for (int i = 0; i < 64; ++i) {
            char c = board[i / 8][i % 8];
            switch (c) {
                case 'k':
                    bitmap.setBitmap(i, ChessPieceType.WHITE_KING);
                    break;
                case 'r':
                    bitmap.setBitmap(i, ChessPieceType.WHITE_ROOK);
                    break;
                case 'b':
                    bitmap.setBitmap(i, ChessPieceType.WHITE_BISHOP);
                    break;
                case 'q':
                    bitmap.setBitmap(i, ChessPieceType.WHITE_QUEEN);
                    break;
                case 'n':
                    bitmap.setBitmap(i, ChessPieceType.WHITE_KNIGHT);
                    break;
                case 'p':
                    bitmap.setBitmap(i, ChessPieceType.WHITE_PAWN);
                    break;
                case 'K':
                    bitmap.setBitmap(i, ChessPieceType.BLACK_KING);
                    break;
                case 'R':
                    bitmap.setBitmap(i, ChessPieceType.BLACK_ROOK);
                    break;
                case 'B':
                    bitmap.setBitmap(i, ChessPieceType.BLACK_BISHOP);
                    break;
                case 'Q':
                    bitmap.setBitmap(i, ChessPieceType.BLACK_QUEEN);
                    break;
                case 'N':
                    bitmap.setBitmap(i, ChessPieceType.BLACK_KNIGHT);
                    break;
                case 'P':
                    bitmap.setBitmap(i, ChessPieceType.BLACK_PAWN);
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
