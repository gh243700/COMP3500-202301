package academy.pocu.comp3500.assignment3;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Bitmap {
    private long[] board = new long[12];
    private static final char[] LETTERS = {'k', 'r', 'b', 'q', 'n', 'p', 'K', 'R', 'B', 'Q', 'N', 'P'};
    private static final int[] VALUES = {200, 5, 3, 9, 3, 1, 200, 5, 3, 9, 3, 1};
    private int whiteKingCount = 0;
    private int whiteRookCount = 0;
    private int whiteBishopCount = 0;
    private int whiteQueenCount = 0;
    private int whiteKnightCount = 0;
    private int whitePawnCount = 0;
    private int blackKingCount = 0;
    private int blackRookCount = 0;
    private int blackBishopCount = 0;
    private int blackQueenCount = 0;
    private int blackKnightCount = 0;
    private int blackPawnCount = 0;
    private int[] counts = {
            whiteKingCount,
            whiteRookCount,
            whiteBishopCount,
            whiteQueenCount,
            whiteKnightCount,
            whitePawnCount,
            blackKingCount,
            blackRookCount,
            blackBishopCount,
            blackQueenCount,
            blackKnightCount,
            blackPawnCount
    };

    private ArrayList<ChessPiece> chessPieces = new ArrayList<>(32);
    private ArrayList<ChessPiece> removed = new ArrayList<>(32);

    public ArrayList<ChessPiece> getChessPieces() {
        return chessPieces;
    }

    public void decreesCount(ChessPieceType type) {
        --counts[type.ordinal()];
    }

    public void increaseCount(ChessPieceType type) {
        ++counts[type.ordinal()];
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

        for (int i = 0; i < removed.size(); ++i) {
            ChessPiece chessPiece = removed.get(i);
            if (chessPiece.getType() == type) {
                chessPiece.setOffset(offset);
                removed.remove(chessPiece);
                chessPieces.add(chessPiece);
            }


        }

        board[type.ordinal()] = board[type.ordinal()] | mask;
    }

    public void off(final int offset, final ChessPieceType type) {
        if (type == ChessPieceType.NONE) {
            return;
        }

        long mask = 0x01;
        mask = mask << offset;
        mask = ~mask;

        for (int i = 0; i < chessPieces.size(); ++i) {
            ChessPiece chessPiece = chessPieces.get(i);
            if (chessPiece.getOffset() == offset && chessPiece.getType() == type) {
                chessPiece.setOffset(-1);
                chessPieces.remove(chessPiece);
                removed.add(chessPiece);
                break;
            }
        }

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
                whiteScore += counts[i] * VALUES[i];
            } else {
                blackScore += counts[i] * VALUES[i];
            }
        }

        return whiteScore - blackScore;
    }


    public void convertToBitmap(char[][] board) {
        chessPieces.clear();
        removed.clear();

        for (int i = 0; i < 12; ++i) {
            this.board[i] = this.board[i] & 0x0;
        }

        for (int i = 0; i < 64; ++i) {
            char c = board[i / 8][i % 8];
            switch (c) {
                case 'k':
                    chessPieces.add(new ChessPiece(ChessPieceType.WHITE_KING, i));
                    ++whiteKingCount;
                    on(i, ChessPieceType.WHITE_KING);
                    break;
                case 'r':
                    chessPieces.add(new ChessPiece(ChessPieceType.WHITE_ROOK, i));
                    ++whiteRookCount;
                    on(i, ChessPieceType.WHITE_ROOK);
                    break;
                case 'b':
                    chessPieces.add(new ChessPiece(ChessPieceType.WHITE_BISHOP, i));
                    ++whiteBishopCount;
                    on(i, ChessPieceType.WHITE_BISHOP);
                    break;
                case 'q':
                    chessPieces.add(new ChessPiece(ChessPieceType.WHITE_QUEEN, i));
                    ++whiteQueenCount;
                    on(i, ChessPieceType.WHITE_QUEEN);
                    break;
                case 'n':
                    chessPieces.add(new ChessPiece(ChessPieceType.WHITE_KNIGHT, i));
                    ++whiteKnightCount;
                    on(i, ChessPieceType.WHITE_KNIGHT);
                    break;
                case 'p':
                    chessPieces.add(new ChessPiece(ChessPieceType.WHITE_PAWN, i));
                    ++whitePawnCount;
                    on(i, ChessPieceType.WHITE_PAWN);
                    break;
                case 'K':
                    chessPieces.add(new ChessPiece(ChessPieceType.BLACK_KING, i));
                    ++blackKingCount;
                    on(i, ChessPieceType.BLACK_KING);
                    break;
                case 'R':
                    chessPieces.add(new ChessPiece(ChessPieceType.BLACK_ROOK, i));
                    ++blackRookCount;
                    on(i, ChessPieceType.BLACK_ROOK);
                    break;
                case 'B':
                    chessPieces.add(new ChessPiece(ChessPieceType.BLACK_BISHOP, i));
                    ++blackBishopCount;
                    on(i, ChessPieceType.BLACK_BISHOP);
                    break;
                case 'Q':
                    chessPieces.add(new ChessPiece(ChessPieceType.BLACK_QUEEN, i));
                    ++blackQueenCount;
                    on(i, ChessPieceType.BLACK_QUEEN);
                    break;
                case 'N':
                    chessPieces.add(new ChessPiece(ChessPieceType.BLACK_KNIGHT, i));
                    ++blackKingCount;
                    on(i, ChessPieceType.BLACK_KNIGHT);
                    break;
                case 'P':
                    ++blackPawnCount;
                    chessPieces.add(new ChessPiece(ChessPieceType.BLACK_PAWN, i));
                    on(i, ChessPieceType.BLACK_PAWN);
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