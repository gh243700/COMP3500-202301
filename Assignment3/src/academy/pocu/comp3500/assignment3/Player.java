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

    public Player(boolean isWhite, int maxMoveTimeMilliseconds) {
        super(isWhite, maxMoveTimeMilliseconds);
        depth = 5;
    }

    @Override
    public Move getNextMove(char[][] board) {
        long start = System.nanoTime();

        //int bak = this.depth;
        //this.depth = 2;
        Move[] finalResult = new Move[1];
        //int bestEvaluation = minimax(board, depth, isWhite(), start, finalResult);
        //Move bestMove = finalResult[0];

        //this.depth = 2;
        //int tempEvaluation = minimax(board, depth, isWhite(), start, finalResult);
        //Move tempMove = finalResult[0];

        //if (isWhite() ? bestEvaluation <= tempEvaluation : bestEvaluation >= tempEvaluation) {
        //    bestEvaluation = tempEvaluation;
        //    bestMove = tempMove;
        //}

        //this.depth = bak;
        int bestEvaluation = minimax(board, depth, isWhite(), start, finalResult);
        Move bestMove = finalResult[0];

        //if (isWhite() ? bestEvaluation < tempEvaluation : bestEvaluation > tempEvaluation) {
        //    bestMove = tempMove;
        //}

        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (duration >= getMaxMoveTimeMilliseconds()) {
            //--depth;
        } else {
            ++depth;
        }


        return bestMove;
    }

    @Override
    public Move getNextMove(char[][] board, Move opponentMove) {
        return getNextMove(board);
    }

    public int minimax(char[][] board, int depth, boolean maximizingPlayer, long start, Move[] finalResult) {
        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (depth == 0 || duration >= getMaxMoveTimeMilliseconds() * 8 / 10 || GameOver(board)) {
            return evaluate(board);
        }

        int[] maxEval = {(maximizingPlayer) ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE};

        boolean noResult = false;
        for (int i = 0; i < 64; ++i) {
            int index = maximizingPlayer ? i : 64 - 1 - i;
            ChessPieceType chessPieceType = getChessPieceType(board[index / 8][index % 8]);

            if (maximizingPlayer && Color.chessPieceColor(chessPieceType) == Color.BLACK || !maximizingPlayer && Color.chessPieceColor(chessPieceType) == Color.WHITE || chessPieceType == ChessPieceType.NONE) {
                continue;
            }

            noResult = movesBitmapVersion(board, index, chessPieceType, depth, maximizingPlayer, start, finalResult, maxEval);
        }

        if (noResult) {
            return evaluate(board);
        }

        int eval = evaluate(board);
        if ((maximizingPlayer) ? maxEval[0] < eval : maxEval[0] > eval) {
            maxEval[0] = eval;
        }

        return maxEval[0];
    }

    public boolean movesBitmapVersion(char[][] board, final int offset, final ChessPieceType chessPieceType, int depth, boolean maximizingPlayer, long start, Move[] finalResult, int[] maxEval) {

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
                boolean noMove = pawnMovesBitmapVersion(board, offset, depth, maximizingPlayer, start, finalResult, maxEval);
                boolean noAttack = pawnAttacksBitmapVersion(board, offset, depth, maximizingPlayer, start, finalResult, maxEval);
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

                if (maximizingPlayer && c1Color == Color.WHITE || !maximizingPlayer && c1Color == Color.BLACK) {
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

                int currentEval = minimax(board, depth - 1, maximizingPlayer ? false : true, start, finalResult);

                // undo move
                board[offset / 8][offset % 8] = t1;
                board[offsetAfterMove / 8][offsetAfterMove % 8] = t2;

                if (maximizingPlayer ? currentEval > maxEval[0] : currentEval < maxEval[0]) {
                    maxEval[0] = currentEval;
                    if (isTopDepth) {
                        finalResult[0] = move;
                    }
                }

                if (c1Color != Color.NONE || loopOnce) {
                    break;
                }
            }
        }

        return hasNoResult;
    }

    private boolean pawnMovesBitmapVersion(char[][] board, final int offset, int depth, boolean maximizingPlayer, long start, Move[] finalResult, int[] maxEval) {
        boolean isTopDepth = this.depth == depth;

        boolean hasNoResult = true;
        for (int i = 0; i < PAWN_MOVE_OFFSET.length; ++i) {
            int offsetAfterMove = offset + (maximizingPlayer ? -1 : 1) * PAWN_MOVE_OFFSET[i];
            int y = offset / 8;

            if ((i == 1 && y != (maximizingPlayer ? 6 : 1)) || offsetAfterMove < 0 || offsetAfterMove >= 64 || Color.chessPieceColor(getChessPieceType(board, offsetAfterMove)) != Color.NONE) {
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

            int currentEval = minimax(board, depth - 1, maximizingPlayer ? false : true, start, finalResult);

            // undo move
            board[offset / 8][offset % 8] = t1;
            board[offsetAfterMove / 8][offsetAfterMove % 8] = t2;

            if (maximizingPlayer ? currentEval > maxEval[0] : currentEval < maxEval[0]) {
                maxEval[0] = currentEval;
                if (isTopDepth) {
                    finalResult[0] = move;
                }
            }

        }

        return hasNoResult;
    }

    private boolean pawnAttacksBitmapVersion(char[][] board, final int offset, int depth, boolean maximizingPlayer, long start, Move[] finalResult, int[] maxEval) {
        boolean isTopDepth = this.depth == depth;
        boolean hasNoResult = true;

        for (int i = 0; i < PAWN_ATTACK_OFFSET.length; ++i) {
            int x = 8 * (7 - offset % 8) + offset / 8;
            x += (maximizingPlayer ? -1 : 1) * PAWN_ATTACK_BOUND_X[i] * 8;
            int offsetAfterMove = offset + (maximizingPlayer ? -1 : 1) * PAWN_ATTACK_OFFSET[i];

            if (offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                continue;
            }

            ChessPieceType c1 = getChessPieceType(board, offsetAfterMove);
            if (Color.chessPieceColor(c1) != ((maximizingPlayer) ? Color.BLACK : Color.WHITE)) {
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

            board[offset / 8][offset % 8] = 0;
            board[offsetAfterMove / 8][offsetAfterMove % 8] = t1;

            int currentEval = minimax(board, depth - 1, maximizingPlayer ? false : true, start, finalResult);

            // undo move
            board[offset / 8][offset % 8] = t1;
            board[offsetAfterMove / 8][offsetAfterMove % 8] = t2;

            if (maximizingPlayer ? currentEval > maxEval[0] : currentEval < maxEval[0]) {
                maxEval[0] = currentEval;
                if (isTopDepth) {
                    finalResult[0] = move;
                }
            }

            if (c1 != ChessPieceType.NONE) {
                break;
            }
        }

        return hasNoResult;
    }


    public boolean GameOver(char[][] board) {
        int white = 0;
        int black = 0;

        for (int i = 0; i < 64; ++i) {
            if (white > 0 && black > 0) {
                return false;
            }

            if (Color.chessPieceColor(getChessPieceType(board, i)) == Color.WHITE) {
                white++;

            }
            if (Color.chessPieceColor(getChessPieceType(board, i)) == Color.BLACK) {
                black++;
            }
        }

        return white == 0 || black == 0;
    }

    public int evaluate(char[][] board) {
        int whiteScore = 0;
        int blackScore = 0;

        for (int i = 0; i < 64; ++i) {
            if (Color.chessPieceColor(getChessPieceType(board, i)) == Color.BLACK) {
                blackScore += VALUES[getChessPieceType(board, i).ordinal()];
            }
            if (Color.chessPieceColor(getChessPieceType(board, i)) == Color.WHITE) {
                whiteScore += VALUES[getChessPieceType(board, i).ordinal()];
            }
        }

        return whiteScore - blackScore;
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
}