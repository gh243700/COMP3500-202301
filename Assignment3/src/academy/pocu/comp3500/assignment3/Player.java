package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;
import academy.pocu.comp3500.assignment3.chess.PlayerBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Player extends PlayerBase {
    private final static int[] PAWN_MOVE_OFFSET = {8, 16};
    private final static int[] PAWN_ATTACK_OFFSET = {9, 7};
    private final static byte[] PAWN_ATTACK_BOUND_X = {-1, 1};
    private final static int[] KING_QUEEN_MOVE_OFFSET = {1, -1, 8, -8, -7, 9, 7, -9};
    private final static byte[] KING_QUEEN_MOVE_BOUND_X = {1, -1, 0, 0, 1, 1, -1, -1};
    private final static int[] ROOK_MOVE_OFFSET = {1, -1, 8, -8};
    private final static byte[] ROOK_MOVE_BOUND_X = {1, -1, 0, 0};
    private final static int[] BISHOP_MOVE_OFFSET = {-7, 9, 7, -9};
    private final static byte[] BISHOP_MOVE_BOUND_X = {1, 1, -1, -1};
    private final static int[] KNIGHT_MOVE_OFFSET = {6, 10, 15, 17, -6, -10, -15, -17};
    private final static byte[] KNIGHT_MOVE_BOUND_X = {-2, 2, -1, 1, 2, -2, 1, -1};
    private int depth;
    private boolean timeOut;
    private static WrappersPool wrappersPool = WrappersPool.getInstance();
    private static MovesPool movesPool = MovesPool.getInstance();

    public Player(boolean isWhite, int maxMoveTimeMilliseconds) {
        super(isWhite, maxMoveTimeMilliseconds);
        depth = 4;
    }

    @Override
    public Move getNextMove(char[][] board) {
        long start = System.nanoTime();
        if (timeOut) {
            --depth;
            timeOut = false;
        } else {
            ++depth;
        }

        Wrapper wrapper = minimax(Bitmap.convertToBitmap(board), depth, isWhite(), start);
        Move result = wrapper.getMove();
        wrappersPool.delete(wrapper);
        return result;
    }

    @Override
    public Move getNextMove(char[][] board, Move opponentMove) {
        return getNextMove(board);
    }

    public static Move prioritizeProtectingOwnPiece(ArrayList<Move> moves, Bitmap board, boolean isWhite) {
        Move result = null;

        for (Move move : moves) {
            if (isProtectingOwnPiece(move, board, isWhite)) {
                if (result != null) {
                    movesPool.delete(result);
                }
                result = move;

                break;
            } else if (result != null) {
                movesPool.delete(result);
            } else {
                result = move;
            }
        }

        return result;
    }

    public static boolean isProtectingOwnPiece(Move move, Bitmap board, boolean isWhite) {
        // move
        int offsetFrom = move.fromY * 8 + move.fromX;
        int offsetTo = move.toY * 8 + move.toX;

        ChessPieceType t1 = board.getChessPieceType(offsetFrom);

        board.on(offsetTo, t1);
        board.off(offsetFrom, t1);

        boolean result = true;

        for (Move attack : getNextMovesBitmapVer(board, !isWhite)) {
            if (attack.toX == move.toX && attack.toY == move.toY) {
                result = false;
                break;
            }
        }

        // undo
        board.off(offsetTo, t1);
        board.on(offsetFrom, t1);

        return result;
    }

    public Wrapper minimax(Bitmap board, int depth, boolean maximizingPlayer, long start) {

        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (depth == 0 || board.GameOver() || duration >= getMaxMoveTimeMilliseconds()) {
            return wrappersPool.alloc(board.evaluate(), null);
        }

        LinkedList<Move> moves = getNextMovesBitmapVer(board, maximizingPlayer);

        if (moves.size() == 0) {
            return wrappersPool.alloc(board.evaluate(), null);
        }

        Move bestMove = moves.get(0);
        ArrayList<Move> sameMoves = new ArrayList<>(32);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE + 1;
            for (Move move : moves) {
                // make move

                end = System.nanoTime();
                duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

                if (duration >= getMaxMoveTimeMilliseconds()) {
                    break;
                }

                int offsetFrom = move.fromY * 8 + move.fromX;
                int offsetTo = move.toY * 8 + move.toX;

                ChessPieceType t1 = board.getChessPieceType(offsetFrom);
                ChessPieceType t2 = board.getChessPieceType(offsetTo);

                board.on(offsetTo, t1);
                board.off(offsetFrom, t1);
                board.off(offsetTo, t2);

                Wrapper wrapper = minimax(board, depth - 1, false, start);
                int currentEval = wrapper.getEval();

                // undo move
                board.off(offsetTo, t1);
                board.on(offsetFrom, t1);
                board.on(offsetTo, t2);

                if (currentEval > maxEval) {
                    maxEval = currentEval;
                    bestMove = move;
                    sameMoves.clear();
                    sameMoves.add(move);
                } else if (currentEval == maxEval) {
                    sameMoves.add(move);
                } else {
                    movesPool.delete(move);
                }

                wrappersPool.delete(wrapper);
            }

            if (sameMoves.size() > 1) {
                bestMove = prioritizeProtectingOwnPiece(sameMoves, board, true);
            }

            if (maxEval == Integer.MAX_VALUE + 1) {
                maxEval = board.evaluate();
            }

            return wrappersPool.alloc(maxEval, bestMove);
        }

        int minEval = Integer.MAX_VALUE;
        for (Move move : moves) {
            // make move
            end = System.nanoTime();
            duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

            if (duration >= getMaxMoveTimeMilliseconds()) {
                break;
            }

            int offsetFrom = move.fromY * 8 + move.fromX;
            int offsetTo = move.toY * 8 + move.toX;

            ChessPieceType t1 = board.getChessPieceType(offsetFrom);
            ChessPieceType t2 = board.getChessPieceType(offsetTo);

            board.on(offsetTo, t1);
            board.off(offsetFrom, t1);
            board.off(offsetTo, t2);

            Wrapper wrapper = minimax(board, depth - 1, true, start);
            int currentEval = wrapper.getEval();

            // undo move
            board.off(offsetTo, t1);
            board.on(offsetFrom, t1);
            board.on(offsetTo, t2);

            if (currentEval < minEval) {
                minEval = currentEval;
                bestMove = move;


                sameMoves.clear();
                sameMoves.add(move);


            } else if (minEval == currentEval) {
                sameMoves.add(move);
            } else {
                movesPool.delete(move);
            }

            wrappersPool.delete(wrapper);
        }

        if (sameMoves.size() > 1) {
            bestMove = prioritizeProtectingOwnPiece(sameMoves, board, false);
        }

        if (minEval == Integer.MAX_VALUE) {
            minEval = board.evaluate();
        }

        return wrappersPool.alloc(minEval, bestMove);
    }

    public static LinkedList<Move> getNextMovesBitmapVer(Bitmap board, boolean isWhite) {

        LinkedList<Move> result = new LinkedList<>();

        for (int i = 0; i < 64; ++i) {
            ChessPieceType chessPieceType = board.getChessPieceType(i);

            if (isWhite && board.chessPieceColor(i) == Color.BLACK || !isWhite && board.chessPieceColor(i) == Color.WHITE || chessPieceType == ChessPieceType.NONE) {
                continue;
            }

            movesBitmapVersion(i, board, chessPieceType, isWhite, result);
        }

        return result;
    }

    public static void movesBitmapVersion(final int offset, final Bitmap board, final ChessPieceType chessPieceType, final boolean isWhite, LinkedList<Move> result) {
        int[] moveOffset = null;
        byte[] boundX = null;
        boolean loopOnce = false;

        switch (chessPieceType) {
            case BLACK_KING:
            case WHITE_KING:
                loopOnce = true;
            case BLACK_QUEEN:
            case WHITE_QUEEN:
                moveOffset = KING_QUEEN_MOVE_OFFSET;
                boundX = KING_QUEEN_MOVE_BOUND_X;
                break;
            case BLACK_ROOK:
            case WHITE_ROOK:
                moveOffset = ROOK_MOVE_OFFSET;
                boundX = ROOK_MOVE_BOUND_X;
                break;
            case BLACK_BISHOP:
            case WHITE_BISHOP:
                moveOffset = BISHOP_MOVE_OFFSET;
                boundX = BISHOP_MOVE_BOUND_X;
                break;
            case BLACK_KNIGHT:
            case WHITE_KNIGHT:
                loopOnce = true;
                moveOffset = KNIGHT_MOVE_OFFSET;
                boundX = KNIGHT_MOVE_BOUND_X;
                break;
            case BLACK_PAWN:
            case WHITE_PAWN:
                pawnMovesBitmapVersion(offset, board, isWhite, result);
                pawnAttacksBitmapVersion(offset, board, isWhite, result);
                return;
            default:
                assert (false);
                break;
        }

        for (int i = 0; i < moveOffset.length; ++i) {
            int offsetAfterMove = offset;
            while (true) {
                int x = 8 * (7 - offsetAfterMove % 8) + offsetAfterMove / 8;
                x += -1 * boundX[i] * 8;
                offsetAfterMove += moveOffset[i];

                if (offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                    break;
                }

                ChessPieceType c1 = board.getChessPieceType(offsetAfterMove);
                Color c1Color = Color.chessPieceColor(c1);

                if (isWhite && c1Color == Color.WHITE || !isWhite && c1Color == Color.BLACK) {
                    break;
                }
                Move move = movesPool.alloc(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);

                if (isWhite && c1Color == Color.BLACK || !isWhite && c1Color == Color.WHITE) {
                    result.addFirst(move);
                } else {
                    result.addLast(move);
                }

                if (c1Color != Color.NONE || loopOnce) {
                    break;
                }
            }
        }
    }

    private static void pawnMovesBitmapVersion(final int offset, final Bitmap board, boolean isWhite, LinkedList<Move> result) {
        for (int i = 0; i < PAWN_MOVE_OFFSET.length; ++i) {
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_MOVE_OFFSET[i];
            int y = offset / 8;

            if ((i == 1 && y != (isWhite ? 6 : 1)) || offsetAfterMove < 0 || offsetAfterMove >= 64 || board.chessPieceColor(offsetAfterMove) != Color.NONE) {
                break;
            }

            result.addLast(movesPool.alloc(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8));
        }
    }

    private static void pawnAttacksBitmapVersion(final int offset, final Bitmap board, boolean isWhite, LinkedList<Move> result) {
        for (int i = 0; i < PAWN_ATTACK_OFFSET.length; ++i) {
            int x = 8 * (7 - offset % 8) + offset / 8;
            x += (isWhite ? -1 : 1) * PAWN_ATTACK_BOUND_X[i] * 8;
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_ATTACK_OFFSET[i];

            ChessPieceType c1 = board.getChessPieceType(offsetAfterMove);

            if (board.chessPieceColor(offsetAfterMove) != ((isWhite) ? Color.BLACK : Color.WHITE) || offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                continue;
            }

            Color c1Color = Color.chessPieceColor(c1);

            Move move = movesPool.alloc(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);

            if (isWhite && c1Color == Color.BLACK || !isWhite && c1Color == Color.WHITE) {
                result.addFirst(move);
            } else {
                result.addLast(move);
            }

            if (c1 != ChessPieceType.NONE) {
                break;
            }
        }
    }
}
