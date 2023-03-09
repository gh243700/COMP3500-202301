package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;
import academy.pocu.comp3500.assignment3.chess.PlayerBase;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class Player extends PlayerBase {
    private final static byte[] PAWN_MOVE_OFFSET = {8, 16};
    private final static byte[] PAWN_ATTACK_OFFSET = {9, 7};
    private final static byte[] PAWN_ATTACK_BOUND_X = {-1, 1};
    private final static byte[] KING_QUEEN_MOVE_OFFSET = {1, -1, 8, -8, -7, 9, 7, -9};
    private final static byte[] KING_QUEEN_MOVE_BOUND_X = {1, -1, 0, 0, 1, 1, -1, -1};
    private final static byte[] ROOK_MOVE_OFFSET = {1, -1, 8, -8};
    private final static byte[] ROOK_MOVE_BOUND_X = {1, -1, 0, 0};
    private final static byte[] BISHOP_MOVE_OFFSET = {-7, 9, 7, -9};
    private final static byte[] BISHOP_MOVE_BOUND_X = {1, 1, -1, -1};
    private final static byte[] KNIGHT_MOVE_OFFSET = {6, 10, 15, 17, -6, -10, -15, -17};
    private final static byte[] KNIGHT_MOVE_BOUND_X = {-2, 2, -1, 1, 2, -2, 1, -1};
    private WrappersPool wrappersPool = WrappersPool.getInstance();
    private MovesPool movesPool = MovesPool.getInstance();
    private int depth;
    private Bitmap bitmap;
    private Stack<Move> moveStack = new Stack<>();

    public Player(boolean isWhite, int maxMoveTimeMilliseconds) {
        super(isWhite, maxMoveTimeMilliseconds);
        depth = 5;
        bitmap = new Bitmap();

        WrappersPool.reset();
        MovesPool.reset();
    }

    @Override
    public Move getNextMove(char[][] board) {
        long start = System.nanoTime();

        bitmap.convertToBitmap(board);
        Wrapper wrapper = minimax(bitmap, depth, isWhite(), start);
        Move result = wrapper.getMove();
        wrappersPool.delete(wrapper);

        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (duration >= getMaxMoveTimeMilliseconds() * 2 / 3) {
            --depth;
        } else {
            ++depth;
        }

        return result;
    }

    @Override
    public Move getNextMove(char[][] board, Move opponentMove) {
        return getNextMove(board);
    }

    public Move prioritizeMove(boolean isWhite, Move m1, Move m2) {
        int bestEvaluation = Integer.MIN_VALUE;
        Move bestMove = m1;

        for (int i = 0; i < 2; ++i) {
            Move move = (i == 0) ? m1 : m2;
            ChessPieceType chessPieceType = bitmap.getChessPieceType(move.fromX + move.fromY * 8);
            int eval = Bitmap.VALUES[chessPieceType.ordinal()];
            if (isProtectingOwnPiece(move, isWhite)) {
                if (eval > bestEvaluation) {
                    bestEvaluation = eval;
                    bestMove = move;
                }
                continue;
            }

            ChessPieceType enemyTypeOrNone = bitmap.getChessPieceType(move.toX + move.toY * 8);
            if (enemyTypeOrNone == ChessPieceType.NONE) {
                continue;
            }
            int enemyCaptureEval = Bitmap.VALUES[enemyTypeOrNone.ordinal()];
            if (enemyCaptureEval > bestEvaluation) {
                bestEvaluation = enemyCaptureEval;
                bestMove = move;
            }
        }

        Move deletable = bestMove == m1 ? m2 : m1;
        movesPool.delete(deletable);

        return bestMove;
    }

    public boolean isProtectingOwnPiece(Move move, boolean isWhite) {
        // move
        int offsetFrom = move.fromY * 8 + move.fromX;
        int offsetTo = move.toY * 8 + move.toX;
        ChessPieceType t1 = bitmap.getChessPieceType(offsetFrom);
        ChessPieceType t2 = bitmap.getChessPieceType(offsetTo);

        bitmap.on(offsetTo, t1);
        bitmap.off(offsetFrom, t1);
        bitmap.off(offsetTo, t2);

        bitmap.moveChessPiece(offsetFrom, offsetTo, t1);
        bitmap.removeChessPieceFromBoard(offsetTo, t2);

        ArrayList<ChessPiece> chessPieces = bitmap.getChessPieces();

        boolean result = true;

        for (int i = 0; i < chessPieces.size(); ++i) {
            ChessPiece chessPiece = chessPieces.get(i);
            ChessPieceType chessPieceType = chessPiece.getType();
            Color color = Color.chessPieceColor(chessPieceType);

            if (isWhite ? Color.WHITE == color : Color.BLACK == color) {
                continue;
            }

            boolean isSafe = isSafeFromEnemy(chessPiece.getOffset(), offsetTo, chessPieceType, isWhite ? false : true);

            if (isSafe == false) {
                result = false;
                break;
            }
        }

        // undo
        bitmap.off(offsetTo, t1);
        bitmap.on(offsetFrom, t1);
        bitmap.on(offsetTo, t2);

        bitmap.moveChessPiece(offsetTo, offsetFrom, t1);
        bitmap.addChessPieceFromBoard(offsetTo, t2);

        return result;
    }

    public Wrapper minimax(Bitmap board, int depth, boolean maximizingPlayer, long start) {

        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (depth == 0 || board.GameOver() || duration >= getMaxMoveTimeMilliseconds() * 2 / 3) {
            return wrappersPool.alloc(board.evaluate(), null);
        }

        int count = getNextMovesBitmapVer(maximizingPlayer);

        if (count == 0) {
            return wrappersPool.alloc(board.evaluate(), null);
        }

        Move bestMove = moveStack.peek();
        boolean foundBestMove = false;

        boolean isTopDepth = this.depth == depth;

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE + 1;
            for (int i = 0; i < count; ++i) {
                // make move
                Move move = moveStack.pop();
                int offsetFrom = move.fromY * 8 + move.fromX;
                int offsetTo = move.toY * 8 + move.toX;

                ChessPieceType t1 = board.getChessPieceType(offsetFrom);
                ChessPieceType t2 = board.getChessPieceType(offsetTo);

                board.on(offsetTo, t1);
                board.off(offsetFrom, t1);
                board.off(offsetTo, t2);

                board.moveChessPiece(offsetFrom, offsetTo, t1);
                board.removeChessPieceFromBoard(offsetTo, t2);

                Wrapper wrapper = minimax(board, depth - 1, false, start);
                int currentEval = wrapper.getEval();

                // undo move
                board.off(offsetTo, t1);
                board.on(offsetFrom, t1);
                board.on(offsetTo, t2);

                board.moveChessPiece(offsetTo, offsetFrom, t1);
                board.addChessPieceFromBoard(offsetTo, t2);

                if (currentEval > maxEval) {
                    if (foundBestMove) {
                        movesPool.delete(bestMove);
                    }

                    maxEval = currentEval;
                    bestMove = move;
                    foundBestMove = true;
                } else if (isTopDepth && currentEval == maxEval) {
                    bestMove = prioritizeMove(true, bestMove, move);
                } else {
                    movesPool.delete(move);
                }

                wrappersPool.delete(wrapper);
            }

            return wrappersPool.alloc(maxEval, bestMove);
        }

        int minEval = Integer.MAX_VALUE;
        for (int i = 0; i < count; ++i) {
            // make move
            Move move = moveStack.pop();

            int offsetFrom = move.fromY * 8 + move.fromX;
            int offsetTo = move.toY * 8 + move.toX;

            ChessPieceType t1 = board.getChessPieceType(offsetFrom);
            ChessPieceType t2 = board.getChessPieceType(offsetTo);

            board.on(offsetTo, t1);
            board.off(offsetFrom, t1);
            board.off(offsetTo, t2);

            board.moveChessPiece(offsetFrom, offsetTo, t1);
            board.removeChessPieceFromBoard(offsetTo, t2);

            Wrapper wrapper = minimax(board, depth - 1, true, start);
            int currentEval = wrapper.getEval();

            // undo move
            board.off(offsetTo, t1);
            board.on(offsetFrom, t1);
            board.on(offsetTo, t2);

            board.moveChessPiece(offsetTo, offsetFrom, t1);
            board.addChessPieceFromBoard(offsetTo, t2);

            if (currentEval < minEval) {
                minEval = currentEval;
                bestMove = move;
            } else if (isTopDepth && minEval == currentEval) {
                bestMove = prioritizeMove(false, bestMove, move);
            } else {
                movesPool.delete(move);
            }
            wrappersPool.delete(wrapper);
        }

        return wrappersPool.alloc(minEval, bestMove);
    }

    public int getNextMovesBitmapVer(boolean isWhite) {

        int beforeAddCount = moveStack.size();
        ArrayList<ChessPiece> chessPieces = bitmap.getChessPieces();

        for (int i = 0; i < chessPieces.size(); ++i) {
            ChessPiece chessPiece = chessPieces.get(i);
            ChessPieceType chessPieceType = chessPiece.getType();

            if (isWhite && Color.chessPieceColor(chessPieceType) == Color.BLACK || !isWhite && Color.chessPieceColor(chessPieceType) == Color.WHITE || chessPieceType == ChessPieceType.NONE) {
                continue;
            }

            movesBitmapVersion(chessPiece.getOffset(), chessPieceType, isWhite);
        }

        return moveStack.size() - beforeAddCount;
    }

    public void movesBitmapVersion(final int offset, final ChessPieceType chessPieceType, final boolean isWhite) {
        byte[] moveOffset = null;
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
                pawnMovesBitmapVersion(offset, isWhite);
                pawnAttacksBitmapVersion(offset, isWhite);
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

                ChessPieceType c1 = bitmap.getChessPieceType(offsetAfterMove);
                Color c1Color = Color.chessPieceColor(c1);

                if (isWhite && c1Color == Color.WHITE || !isWhite && c1Color == Color.BLACK) {
                    break;
                }
                Move move = movesPool.alloc(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);

                moveStack.add(move);

                if (c1Color != Color.NONE || loopOnce) {
                    break;
                }
            }
        }
    }

    private void pawnMovesBitmapVersion(final int offset, boolean isWhite) {
        for (int i = 0; i < PAWN_MOVE_OFFSET.length; ++i) {
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_MOVE_OFFSET[i];
            int y = offset / 8;

            if ((i == 1 && y != (isWhite ? 6 : 1)) || offsetAfterMove < 0 || offsetAfterMove >= 64 || bitmap.chessPieceColor(offsetAfterMove) != Color.NONE) {
                break;
            }

            moveStack.add(movesPool.alloc(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8));
        }
    }

    private void pawnAttacksBitmapVersion(final int offset, boolean isWhite) {
        for (int i = 0; i < PAWN_ATTACK_OFFSET.length; ++i) {
            int x = 8 * (7 - offset % 8) + offset / 8;
            x += (isWhite ? -1 : 1) * PAWN_ATTACK_BOUND_X[i] * 8;
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_ATTACK_OFFSET[i];

            ChessPieceType c1 = bitmap.getChessPieceType(offsetAfterMove);

            if (bitmap.chessPieceColor(offsetAfterMove) != ((isWhite) ? Color.BLACK : Color.WHITE) || offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                continue;
            }


            Move move = movesPool.alloc(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);
            moveStack.add(move);


            if (c1 != ChessPieceType.NONE) {
                break;
            }
        }
    }

    public boolean isSafeFromEnemy(final int offset, int offsetTo, final ChessPieceType chessPieceType, final boolean isWhite) {
        byte[] moveOffset = null;
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
                return isSafeFromPawn(offset, offsetTo, isWhite);
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

                ChessPieceType c1 = bitmap.getChessPieceType(offsetAfterMove);
                Color c1Color = Color.chessPieceColor(c1);

                if (offsetAfterMove == offsetTo) {
                    return false;
                }

                if (c1Color != Color.NONE || loopOnce) {
                    break;
                }
            }
        }

        return true;
    }

    private boolean isSafeFromPawn(final int offset, int offsetTo, boolean isWhite) {
        for (int i = 0; i < PAWN_ATTACK_OFFSET.length; ++i) {
            int x = 8 * (7 - offset % 8) + offset / 8;
            x += (isWhite ? -1 : 1) * PAWN_ATTACK_BOUND_X[i] * 8;
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_ATTACK_OFFSET[i];

            ChessPieceType c1 = bitmap.getChessPieceType(offsetAfterMove);

            if (offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                continue;
            }

            if (offsetTo == offsetAfterMove) {
                return false;
            }


            if (c1 != ChessPieceType.NONE) {
                break;
            }
        }

        return true;
    }
}