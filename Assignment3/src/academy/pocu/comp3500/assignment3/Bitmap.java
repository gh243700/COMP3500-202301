package academy.pocu.comp3500.assignment3;

import java.util.ArrayList;

public class Bitmap {
    private long[] board = new long[12];
    private static final char[] LETTERS = {'k', 'r', 'b', 'q', 'n', 'p', 'K', 'R', 'B', 'Q', 'N', 'P'};
    public static final int[] VALUES = {200, 5, 3, 9, 3, 1, 200, 5, 3, 9, 3, 1};
    private byte whiteKingCount = 0;
    private byte whiteRookCount = 0;
    private byte whiteBishopCount = 0;
    private byte whiteQueenCount = 0;
    private byte whiteKnightCount = 0;
    private byte whitePawnCount = 0;
    private byte blackKingCount = 0;
    private byte blackRookCount = 0;
    private byte blackBishopCount = 0;
    private byte blackQueenCount = 0;
    private byte blackKnightCount = 0;
    private byte blackPawnCount = 0;
    private byte[] counts = {
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

    private ArrayList<ChessPiece> chessPieces;
    private ArrayList<ChessPiece> removed;

    public long[] getBoard() {
        return board;
    }

    public Bitmap() {
        chessPieces = new ArrayList<>(32);
        removed = new ArrayList<>(32);
        for (int i = 0; i < 32; ++i) {
            removed.add(new ChessPiece(ChessPieceType.NONE, -1));
        }
    }

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

    public void moveChessPiece(int offsetFrom, int offsetTo, ChessPieceType chessPieceType) {

        if (chessPieceType == ChessPieceType.NONE) {
            return;
        }

        for (int i = 0; i < chessPieces.size(); ++i) {
            ChessPiece chessPiece = chessPieces.get(i);
            if (offsetFrom == chessPiece.getOffset() && chessPieceType == chessPiece.getType()) {
                chessPiece.setOffset(offsetTo);
                return;
            }
        }
    }


    public void on(final int offset, final ChessPieceType type) {
        if (type == ChessPieceType.NONE) {
            return;
        }

        long mask = 0x01;
        mask = mask << offset;

        board[type.ordinal()] = board[type.ordinal()] | mask;
    }

    public void addChessPieceFromBoard(final int offset, final ChessPieceType type) {
        if (type == ChessPieceType.NONE) {
            return;
        }

        ChessPiece chessPiece = removed.get(0);
        chessPiece.reset(type, offset);
        removed.remove(chessPiece);
        chessPieces.add(chessPiece);
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


    public void removeChessPieceFromBoard(final int offset, final ChessPieceType type) {
        if (type == ChessPieceType.NONE) {
            return;
        }

        for (int i = 0; i < chessPieces.size(); ++i) {
            ChessPiece chessPiece = chessPieces.get(i);
            if (chessPiece.getOffset() == offset && chessPiece.getType() == type) {
                chessPiece.setOffset(-1);
                chessPieces.remove(chessPiece);
                removed.add(chessPiece);
                break;
            }
        }
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
        removed.addAll(chessPieces);
        chessPieces.clear();

        for (int i = 0; i < 12; ++i) {
            this.board[i] = this.board[i] & 0x0;
        }

        ChessPiece chessPiece;
        for (int i = 0; i < 64; ++i) {
            char c = board[i / 8][i % 8];
            switch (c) {
                case 'k':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.WHITE_KING, i);
                    chessPieces.add(chessPiece);
                    ++whiteKingCount;
                    on(i, ChessPieceType.WHITE_KING);
                    break;
                case 'r':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.WHITE_ROOK, i);
                    chessPieces.add(chessPiece);
                    ++whiteRookCount;
                    on(i, ChessPieceType.WHITE_ROOK);
                    break;
                case 'b':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.WHITE_BISHOP, i);
                    chessPieces.add(chessPiece);

                    ++whiteBishopCount;
                    on(i, ChessPieceType.WHITE_BISHOP);
                    break;
                case 'q':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.WHITE_QUEEN, i);
                    chessPieces.add(chessPiece);
                    ++whiteQueenCount;
                    on(i, ChessPieceType.WHITE_QUEEN);
                    break;
                case 'n':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.WHITE_KNIGHT, i);
                    chessPieces.add(chessPiece);
                    ++whiteKnightCount;
                    on(i, ChessPieceType.WHITE_KNIGHT);
                    break;
                case 'p':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.WHITE_PAWN, i);
                    chessPieces.add(chessPiece);
                    ++whitePawnCount;
                    on(i, ChessPieceType.WHITE_PAWN);
                    break;
                case 'K':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.BLACK_KING, i);
                    chessPieces.add(chessPiece);
                    ++blackKingCount;
                    on(i, ChessPieceType.BLACK_KING);
                    break;
                case 'R':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.BLACK_ROOK, i);
                    chessPieces.add(chessPiece);
                    ++blackRookCount;
                    on(i, ChessPieceType.BLACK_ROOK);
                    break;
                case 'B':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.BLACK_BISHOP, i);
                    chessPieces.add(chessPiece);
                    ++blackBishopCount;
                    on(i, ChessPieceType.BLACK_BISHOP);
                    break;
                case 'Q':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.BLACK_QUEEN, i);
                    chessPieces.add(chessPiece);
                    ++blackQueenCount;
                    on(i, ChessPieceType.BLACK_QUEEN);
                    break;
                case 'N':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.BLACK_KNIGHT, i);
                    chessPieces.add(chessPiece);
                    ++blackKingCount;
                    on(i, ChessPieceType.BLACK_KNIGHT);
                    break;
                case 'P':
                    chessPiece = removed.get(0);
                    removed.remove(0);
                    chessPiece.reset(ChessPieceType.BLACK_PAWN, i);
                    chessPieces.add(chessPiece);
                    ++blackPawnCount;
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
