package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;
import academy.pocu.comp3500.assignment3.chess.PlayerBase;

import java.util.ArrayList;
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

    private Move bestResultMove;
    private int depth;
    private Bitmap bitmap;

    public Player(boolean isWhite, int maxMoveTimeMilliseconds) {
        super(isWhite, maxMoveTimeMilliseconds);
        depth = 5;
        bitmap = new Bitmap();
    }

    @Override
    public Move getNextMove(char[][] board) {
        long start = System.nanoTime();

        int bak = this.depth;
        this.depth = 1;
        bitmap.convertToBitmap(board);
        int result1 = minimax(bitmap, 1, isWhite(), start);
        Move moveResult = new Move(bestResultMove.fromX, bestResultMove.fromY, bestResultMove.toX, bestResultMove.toY);

        this.depth = bak;
        bitmap.convertToBitmap(board);
        int result2= minimax(bitmap, this.depth, isWhite(), start);
        Move moveResult2 = new Move(bestResultMove.fromX, bestResultMove.fromY, bestResultMove.toX, bestResultMove.toY);

        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (duration >= getMaxMoveTimeMilliseconds()) {
            --depth;
        } else {
            ++depth;
        }

        if (isWhite()) {
            if (result1 > result2) {
                return moveResult2;
            }
            return moveResult;
        }
        if (result1 < result2) {
            return moveResult;
        }

        return moveResult2;
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

        boolean result = true;

        for (int i = 0; i < 64; ++i) {
            ChessPieceType chessPieceType = bitmap.getChessPieceType(i);
            Color color = Color.chessPieceColor(chessPieceType);

            if (chessPieceType == ChessPieceType.NONE) {
                continue;
            }

            if (isWhite ? Color.WHITE == color : Color.BLACK == color) {
                continue;
            }

            boolean isSafe = isSafeFromEnemy(i, offsetTo, chessPieceType, isWhite ? false : true);

            if (isSafe == false) {
                result = false;
                break;
            }
        }

        // undo
        bitmap.off(offsetTo, t1);
        bitmap.on(offsetFrom, t1);
        bitmap.on(offsetTo, t2);

        return result;
    }

    public int minimax(Bitmap board, int depth, boolean maximizingPlayer, long start) {
        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (depth == 0 || board.GameOver() || duration >= getMaxMoveTimeMilliseconds() * 2 / 3) {
            return board.evaluate();
        }

        Move bestMove = null;

        boolean isTopDepth = this.depth == depth;

        int maxOrMinEval = (maximizingPlayer) ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE;

        int count = 0;
        for (int i = 0; i < 64; ++i) {
            ChessPieceType chessPieceType = bitmap.getChessPieceType(i);

            if (maximizingPlayer && Color.chessPieceColor(chessPieceType) == Color.BLACK || !maximizingPlayer && Color.chessPieceColor(chessPieceType) == Color.WHITE || chessPieceType == ChessPieceType.NONE) {
                continue;
            }

            final int offset = i;
            boolean isPawn = false;

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
                    isPawn = true;
                    break;
                default:
                    assert (false);
                    break;
            }


            if (!isPawn) {
                for (int k = 0; k < moveOffset.length; ++k) {
                    Move move = null;
                    int offsetAfterMove = offset;
                    while (true) {
                        int x = 8 * (7 - offsetAfterMove % 8) + offsetAfterMove / 8;
                        x += -1 * boundX[k] * 8;
                        offsetAfterMove += moveOffset[k];

                        if (offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                            break;
                        }

                        ChessPieceType c1 = bitmap.getChessPieceType(offsetAfterMove);
                        Color c1Color = Color.chessPieceColor(c1);

                        if (maximizingPlayer && c1Color == Color.WHITE || !maximizingPlayer && c1Color == Color.BLACK) {
                            break;
                        }

                        if (isTopDepth) {
                            move = new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);
                        }

                        ++count;
                        {
                            // make move
                            int offsetFrom = offset;
                            int offsetTo = offsetAfterMove;

                            ChessPieceType t1 = board.getChessPieceType(offsetFrom);
                            ChessPieceType t2 = board.getChessPieceType(offsetTo);

                            board.on(offsetTo, t1);
                            board.off(offsetFrom, t1);
                            board.off(offsetTo, t2);

                            int currentEval = minimax(board, depth - 1, (maximizingPlayer) ? false : true, start);

                            // undo move
                            board.off(offsetTo, t1);
                            board.on(offsetFrom, t1);
                            board.on(offsetTo, t2);

                            if ((maximizingPlayer) ? currentEval > maxOrMinEval : currentEval < maxOrMinEval) {
                                maxOrMinEval = currentEval;
                                bestMove = move;
                            } else if (isTopDepth && currentEval == maxOrMinEval) {
                                bestMove = prioritizeMove((maximizingPlayer) ? true : false, bestMove, move);
                            }
                        }

                        if (c1Color != Color.NONE || loopOnce) {
                            break;
                        }
                    }
                }
            } else {
                Move move = null;
                for (int k = 0; k < PAWN_MOVE_OFFSET.length; ++k) {
                    int offsetAfterMove = offset + (maximizingPlayer ? -1 : 1) * PAWN_MOVE_OFFSET[k];
                    int y = offset / 8;

                    if ((k == 1 && y != (maximizingPlayer ? 6 : 1)) || offsetAfterMove < 0 || offsetAfterMove >= 64 || bitmap.chessPieceColor(offsetAfterMove) != Color.NONE) {
                        break;
                    }

                    if (isTopDepth) {
                        move = new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);
                    }
                    ++count;
                    {
                        // make move
                        int offsetFrom = offset;
                        int offsetTo = offsetAfterMove;

                        ChessPieceType t1 = board.getChessPieceType(offsetFrom);
                        ChessPieceType t2 = board.getChessPieceType(offsetTo);

                        board.on(offsetTo, t1);
                        board.off(offsetFrom, t1);
                        board.off(offsetTo, t2);


                        int currentEval = minimax(board, depth - 1, (maximizingPlayer) ? false : true, start);

                        // undo move
                        board.off(offsetTo, t1);
                        board.on(offsetFrom, t1);
                        board.on(offsetTo, t2);

                        if ((maximizingPlayer) ? currentEval > maxOrMinEval : currentEval < maxOrMinEval) {
                            maxOrMinEval = currentEval;
                            bestMove = move;
                        } else if (isTopDepth && currentEval == maxOrMinEval) {
                            bestMove = prioritizeMove((maximizingPlayer) ? true : false, bestMove, move);
                        }
                    }

                }

                for (int k = 0; k < PAWN_ATTACK_OFFSET.length; ++k) {
                    int x = 8 * (7 - offset % 8) + offset / 8;
                    x += (maximizingPlayer ? -1 : 1) * PAWN_ATTACK_BOUND_X[k] * 8;
                    int offsetAfterMove = offset + (maximizingPlayer ? -1 : 1) * PAWN_ATTACK_OFFSET[k];

                    ChessPieceType c1 = bitmap.getChessPieceType(offsetAfterMove);

                    if (bitmap.chessPieceColor(offsetAfterMove) != ((maximizingPlayer) ? Color.BLACK : Color.WHITE) || offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                        continue;
                    }

                    if (isTopDepth) {
                        move = new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);
                    }

                    ++count;

                    {
                        // make move
                        int offsetFrom = offset;
                        int offsetTo = offsetAfterMove;

                        ChessPieceType t1 = board.getChessPieceType(offsetFrom);
                        ChessPieceType t2 = board.getChessPieceType(offsetTo);

                        board.on(offsetTo, t1);
                        board.off(offsetFrom, t1);
                        board.off(offsetTo, t2);


                        int currentEval = minimax(board, depth - 1, (maximizingPlayer) ? false : true, start);

                        // undo move
                        board.off(offsetTo, t1);
                        board.on(offsetFrom, t1);
                        board.on(offsetTo, t2);


                        if ((maximizingPlayer) ? currentEval > maxOrMinEval : currentEval < maxOrMinEval) {
                            maxOrMinEval = currentEval;
                            bestMove = move;
                        } else if (isTopDepth && currentEval == maxOrMinEval) {
                            bestMove = prioritizeMove((maximizingPlayer) ? true : false, bestMove, move);
                        }
                    }

                    if (c1 != ChessPieceType.NONE) {
                        break;
                    }
                }
            }

        }

        if (count == 0) {
            return board.evaluate();
        }

        if (isTopDepth) {
            bestResultMove = bestMove;
        }

        return maxOrMinEval;
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