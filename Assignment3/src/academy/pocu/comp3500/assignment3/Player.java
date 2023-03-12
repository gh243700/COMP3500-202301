package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;
import academy.pocu.comp3500.assignment3.chess.PlayerBase;

import java.util.concurrent.TimeUnit;

public class Player extends PlayerBase {
    public static final int[] VALUES = {200, 5, 3, 9, 3, 1, 200, 5, 3, 9, 3, 1};
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
    private int depth;
    private boolean isTimeOut = false;

    public Player(boolean isWhite, int maxMoveTimeMilliseconds) {
        super(isWhite, maxMoveTimeMilliseconds);
        depth = 5;
    }

    public int[] init(char[][] board) {
        int[] values = new int[4];

        int whiteScore = 0;
        int blackScore = 0;

        int whiteCount = 0;
        int blackCount = 0;

        for (int i = 0; i < 64; ++i) {
            if (Color.chessPieceColor(getChessPieceType(board, i)) == Color.BLACK) {
                blackScore += VALUES[getChessPieceType(board, i).ordinal()];
                ++blackCount;
            }
            if (Color.chessPieceColor(getChessPieceType(board, i)) == Color.WHITE) {
                whiteScore += VALUES[getChessPieceType(board, i).ordinal()];
                ++whiteCount;
            }
        }

        values[0] = whiteScore;
        values[1] = blackScore;
        values[2] = whiteCount;
        values[3] = blackCount;

        return values;
    }

    public boolean isProtectingOwnPiece(char[][] board, Move move) {

        // make move
        char t1 = board[move.fromY][move.fromX];
        char t2 = board[move.toY][move.toX];

        board[move.fromY][move.fromX] = 0;
        board[move.toY][move.toX] = t1;

        boolean isSafe = isSafe(board, move.toY * 8 + move.toX, isWhite() ? false : true);

        board[move.fromY][move.fromX] = t1;
        board[move.toY][move.toX] = t2;

        return isSafe;
    }

    // 평가값이 같은 경우 자신의 말을 보호하는 움직임을 우선순위로 결정하는 함수
    public Move prioritizeProtectingOwnPiece(char[][] board, Move m1, Move m2) {

        Move bestMove = m1;
        int bestEval = Integer.MIN_VALUE;

        for (int i = 0; i < 2; ++i) {
            Move move = (i == 0) ? m1 : m2;
            ChessPieceType chessPieceType = getChessPieceType(board, move.fromY * 8 + move.fromX);
            int evalSelf = VALUES[chessPieceType.ordinal()];
            if (isProtectingOwnPiece(board, move)) {
                bestMove = move;
            } else {
                ChessPieceType other = getChessPieceType(board, move.toY * 8 + move.toX);
                if (other == ChessPieceType.NONE) {
                    continue;
                }

                int eval = VALUES[other.ordinal()];
                if (evalSelf < eval) {
                    bestMove = move;
                    bestEval = eval;
                }
            }
        }

        return bestMove;
    }


    @Override
    public Move getNextMove(char[][] board) {
        long start = System.nanoTime();

        Move[] finalResult = new Move[1];
        int[] values = init(board);

        int bestEvaluation = minimax(board, depth, true, isWhite(), start, finalResult, values, (char) 0);
        Move bestMove = finalResult[0];


        //if (isTimeOut) {
        //    --depth;
        //    isTimeOut = false;
        //} else {
        //    ++depth;
        //
        // }

        return bestMove;
    }

    @Override
    public Move getNextMove(char[][] board, Move opponentMove) {
        return getNextMove(board);
    }

    public int minimax(char[][] board, int depth, boolean maximizingPlayer, boolean isWhite, long start, Move[] finalResult, int[] values, char t2) {
        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (duration >= getMaxMoveTimeMilliseconds() * 9 / 10) {
            isTimeOut = true;
            return evaluate(values);
        }

        if (depth == 0 || GameOver(values, t2)) {
            return evaluate(values);
        }

        int[] maxEval = {(maximizingPlayer) ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE};

        boolean noResult = false;
        int count = 0;
        for (int i = 0; i < 64; ++i) {
            int index = isWhite ? i : 64 - 1 - i;
            ChessPieceType chessPieceType = getChessPieceType(board[index / 8][index % 8]);

            if (isWhite && Color.chessPieceColor(chessPieceType) == Color.BLACK || !isWhite && Color.chessPieceColor(chessPieceType) == Color.WHITE || chessPieceType == ChessPieceType.NONE) {
                continue;
            }

            //if (count >= (isWhite ? values[2] : values[3])) {
            //    break;
            //}

            noResult = movesBitmapVersion(board, index, chessPieceType, depth, maximizingPlayer, isWhite, start, finalResult, maxEval, values);
            ++count;
        }

        if (noResult) {
            return evaluate(values);
        }

        int eval = evaluate(values);
        //if (maximizingPlayer ? eval > maxEval[0] : eval < maxEval[0]) {
        //    maxEval[0] = eval;
        //}

        return maxEval[0];
    }

    public boolean movesBitmapVersion(char[][] board, final int offset, final ChessPieceType chessPieceType, int depth, boolean maximizingPlayer, boolean isWhite, long start, Move[] finalResult, int[] maxEval, int[] values) {

        boolean isTopDepth = this.depth == depth;
        byte[] moveOffset = null;
        byte[] boundX = null;
        boolean loopOnce = false;
        boolean hasNoResult = true;

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
                boolean noMove = pawnMovesBitmapVersion(board, offset, depth, maximizingPlayer, isWhite, start, finalResult, maxEval, values);
                boolean noAttack = pawnAttacksBitmapVersion(board, offset, depth, maximizingPlayer, isWhite, start, finalResult, maxEval, values);
                return noMove && noAttack;
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

                ChessPieceType c1 = getChessPieceType(board, offsetAfterMove);
                Color c1Color = Color.chessPieceColor(c1);

                if (isWhite && c1Color == Color.WHITE || !isWhite && c1Color == Color.BLACK) {
                    break;
                }

                Move move = null;

                hasNoResult = false;
                if (isTopDepth) {
                    move = new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);
                }

                // make move
                char t1 = board[offset / 8][offset % 8];
                char t2 = board[offsetAfterMove / 8][offsetAfterMove % 8];

                if (t2 != 0) {
                    if (isWhite) {
                        values[1] -= VALUES[getChessPieceType(t2).ordinal()];
                        --values[3];
                    } else {
                        values[0] -= VALUES[getChessPieceType(t2).ordinal()];
                        --values[2];
                    }
                }

                board[offset / 8][offset % 8] = 0;
                board[offsetAfterMove / 8][offsetAfterMove % 8] = t1;

                int currentEval = minimax(board, depth - 1, !maximizingPlayer, !isWhite, start, finalResult, values, t2);

                // undo move
                board[offset / 8][offset % 8] = t1;
                board[offsetAfterMove / 8][offsetAfterMove % 8] = t2;

                if (t2 != 0) {
                    if (isWhite) {
                        values[1] += VALUES[getChessPieceType(t2).ordinal()];
                        ++values[3];
                    } else {
                        values[0] += VALUES[getChessPieceType(t2).ordinal()];
                        ++values[2];
                    }
                }

                if (maximizingPlayer ? currentEval > maxEval[0] : currentEval < maxEval[0]) {
                    maxEval[0] = currentEval;
                    if (isTopDepth) {
                        finalResult[0] = move;
                    }
                } else if (maxEval[0] == currentEval) {
                    if (isTopDepth) {
                        finalResult[0] = prioritizeProtectingOwnPiece(board, finalResult[0], move);
                    }
                }

                if (c1Color != Color.NONE || loopOnce) {
                    break;
                }
            }
        }

        return hasNoResult;
    }

    private boolean pawnMovesBitmapVersion(char[][] board, final int offset, int depth, boolean maximizingPlayer, boolean isWhite, long start, Move[] finalResult, int[] maxEval, int[] values) {
        boolean isTopDepth = this.depth == depth;

        boolean hasNoResult = true;
        for (int i = 0; i < PAWN_MOVE_OFFSET.length; ++i) {
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_MOVE_OFFSET[i];
            int y = offset / 8;

            if ((i == 1 && y != (isWhite ? 6 : 1)) || offsetAfterMove < 0 || offsetAfterMove >= 64 || Color.chessPieceColor(getChessPieceType(board, offsetAfterMove)) != Color.NONE) {
                break;
            }

            Move move = null;
            hasNoResult = false;

            if (isTopDepth) {
                move = new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);
            }
            // make move
            char t1 = board[offset / 8][offset % 8];
            char t2 = board[offsetAfterMove / 8][offsetAfterMove % 8];

            board[offset / 8][offset % 8] = 0;
            board[offsetAfterMove / 8][offsetAfterMove % 8] = t1;

            int currentEval = minimax(board, depth - 1, !maximizingPlayer, !isWhite, start, finalResult, values, t2);

            // undo move
            board[offset / 8][offset % 8] = t1;
            board[offsetAfterMove / 8][offsetAfterMove % 8] = t2;

            if (maximizingPlayer ? currentEval > maxEval[0] : currentEval < maxEval[0]) {
                maxEval[0] = currentEval;
                if (isTopDepth) {
                    finalResult[0] = move;
                }
            }else if (maxEval[0] == currentEval) {
                if (isTopDepth) {
                    if (finalResult[0] == null) {
                        System.out.println();
                    }
                    finalResult[0] = prioritizeProtectingOwnPiece(board, finalResult[0], move);
                }
            }
        }

        return hasNoResult;
    }

    private boolean pawnAttacksBitmapVersion(char[][] board, final int offset, int depth, boolean maximizingPlayer, boolean isWhite, long start, Move[] finalResult, int[] maxEval, int[] values) {
        boolean isTopDepth = this.depth == depth;
        boolean hasNoResult = true;

        for (int i = 0; i < PAWN_ATTACK_OFFSET.length; ++i) {
            int x = 8 * (7 - offset % 8) + offset / 8;
            x += (isWhite ? -1 : 1) * PAWN_ATTACK_BOUND_X[i] * 8;
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_ATTACK_OFFSET[i];

            if (offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                continue;
            }

            ChessPieceType c1 = getChessPieceType(board, offsetAfterMove);
            if (Color.chessPieceColor(c1) != ((isWhite) ? Color.BLACK : Color.WHITE)) {
                continue;
            }

            Move move = null;
            hasNoResult = false;
            if (isTopDepth) {
                move = new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8);
            }

            // make move
            char t1 = board[offset / 8][offset % 8];
            char t2 = board[offsetAfterMove / 8][offsetAfterMove % 8];

            if (t2 != 0) {
                if (isWhite) {
                    values[1] -= VALUES[getChessPieceType(t2).ordinal()];
                    --values[3];
                } else {
                    values[0] -= VALUES[getChessPieceType(t2).ordinal()];
                    --values[2];
                }
            }

            board[offset / 8][offset % 8] = 0;
            board[offsetAfterMove / 8][offsetAfterMove % 8] = t1;

            int currentEval = minimax(board, depth - 1, !maximizingPlayer, !isWhite, start, finalResult, values, t2);

            // undo move
            board[offset / 8][offset % 8] = t1;
            board[offsetAfterMove / 8][offsetAfterMove % 8] = t2;

            if (t2 != 0) {
                if (isWhite) {
                    values[1] += VALUES[getChessPieceType(t2).ordinal()];
                    ++values[3];
                } else {
                    values[0] += VALUES[getChessPieceType(t2).ordinal()];
                    ++values[2];
                }
            }

            if (maximizingPlayer ? currentEval > maxEval[0] : currentEval < maxEval[0]) {
                maxEval[0] = currentEval;
                if (isTopDepth) {
                    finalResult[0] = move;
                }
            } else if (maxEval[0] == currentEval) {
                if (isTopDepth) {
                    finalResult[0] = prioritizeProtectingOwnPiece(board, finalResult[0], move);
                }
            }

            if (c1 != ChessPieceType.NONE) {
                break;
            }
        }

        return hasNoResult;
    }


    public boolean GameOver(int[] values, char c1) {

        if (c1 == 'K' || c1 == 'k') {
            return true;
        }

        return values[0] == 0 || values[1] == 0;
    }

    public int evaluate(int[] values) {
        return (isWhite()) ? values[0] - values[1] : values[1] - values[0];
    }

    public ChessPieceType getChessPieceType(char c) {
        switch (c) {
            case 'k':
                return ChessPieceType.WHITE_KING;
            case 'r':
                return ChessPieceType.WHITE_ROOK;
            case 'b':
                return ChessPieceType.WHITE_BISHOP;
            case 'q':
                return ChessPieceType.WHITE_QUEEN;
            case 'n':
                return ChessPieceType.WHITE_KNIGHT;
            case 'p':
                return ChessPieceType.WHITE_PAWN;
            case 'K':
                return ChessPieceType.BLACK_KING;
            case 'R':
                return ChessPieceType.BLACK_ROOK;
            case 'B':
                return ChessPieceType.BLACK_BISHOP;
            case 'Q':
                return ChessPieceType.BLACK_QUEEN;
            case 'N':
                return ChessPieceType.BLACK_KNIGHT;
            case 'P':
                return ChessPieceType.BLACK_PAWN;
            default:
                return ChessPieceType.NONE;
        }
    }

    public ChessPieceType getChessPieceType(char[][] board, int index) {
        switch (board[index / 8][index % 8]) {
            case 'k':
                return ChessPieceType.WHITE_KING;
            case 'r':
                return ChessPieceType.WHITE_ROOK;
            case 'b':
                return ChessPieceType.WHITE_BISHOP;
            case 'q':
                return ChessPieceType.WHITE_QUEEN;
            case 'n':
                return ChessPieceType.WHITE_KNIGHT;
            case 'p':
                return ChessPieceType.WHITE_PAWN;
            case 'K':
                return ChessPieceType.BLACK_KING;
            case 'R':
                return ChessPieceType.BLACK_ROOK;
            case 'B':
                return ChessPieceType.BLACK_BISHOP;
            case 'Q':
                return ChessPieceType.BLACK_QUEEN;
            case 'N':
                return ChessPieceType.BLACK_KNIGHT;
            case 'P':
                return ChessPieceType.BLACK_PAWN;
            default:
                return ChessPieceType.NONE;
        }
    }

    public boolean isSafe(char[][] board, int targetOffset, boolean isWhite) {

        for (int i = 0; i < 64; ++i) {
            ChessPieceType chessPieceType = getChessPieceType(board[i / 8][i % 8]);

            if (isWhite && Color.chessPieceColor(chessPieceType) == Color.BLACK || !isWhite && Color.chessPieceColor(chessPieceType) == Color.WHITE || chessPieceType == ChessPieceType.NONE) {
                continue;
            }

            boolean isSafe = isSafeHelper(board, i, targetOffset, chessPieceType, isWhite);
            if (isSafe == false) {
                return false;
            }

        }

        return true;
    }

    public boolean isSafeHelper(char[][] board, final int offset, final int targetOffset, final ChessPieceType chessPieceType, boolean isWhite) {
        byte[] moveOffset = null;
        byte[] boundX = null;
        boolean loopOnce = false;
        boolean isPawn = false;

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
            for (int i = 0; i < moveOffset.length; ++i) {
                int offsetAfterMove = offset;
                while (true) {
                    int x = 8 * (7 - offsetAfterMove % 8) + offsetAfterMove / 8;
                    x += -1 * boundX[i] * 8;
                    offsetAfterMove += moveOffset[i];

                    if (offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                        break;
                    }

                    ChessPieceType c1 = getChessPieceType(board, offsetAfterMove);
                    Color c1Color = Color.chessPieceColor(c1);

                    if (isWhite && c1Color == Color.WHITE || !isWhite && c1Color == Color.BLACK) {
                        break;
                    }

                    if (c1Color == Color.NONE) {
                        if (loopOnce) {
                            break;
                        }

                        continue;
                    }

                    if (offsetAfterMove == targetOffset) {
                        return false;
                    }

                    if (c1Color != Color.NONE || loopOnce) {
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < PAWN_ATTACK_OFFSET.length; ++i) {
                int x = 8 * (7 - offset % 8) + offset / 8;
                x += (!isWhite() ? -1 : 1) * PAWN_ATTACK_BOUND_X[i] * 8;
                int offsetAfterMove = offset + (!isWhite() ? -1 : 1) * PAWN_ATTACK_OFFSET[i];

                if (offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                    continue;
                }

                ChessPieceType c1 = getChessPieceType(board, offsetAfterMove);
                if (Color.chessPieceColor(c1) != ((!isWhite()) ? Color.BLACK : Color.WHITE)) {
                    continue;
                }

                if (offsetAfterMove == targetOffset) {
                    return false;
                }

                if (c1 != ChessPieceType.NONE) {
                    break;
                }
            }
        }


        return true;
    }
}