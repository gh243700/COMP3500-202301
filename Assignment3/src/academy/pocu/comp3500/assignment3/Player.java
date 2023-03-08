package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;
import academy.pocu.comp3500.assignment3.chess.PlayerBase;

import java.util.*;
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
    private static NodePool nodePool = NodePool.getInstance();

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

        Wrapper wrapper = minimax(board, depth, isWhite());


        if (wrapper.move == null) {
            System.out.println();
        }
        return wrapper.move;
    }

    @Override
    public Move getNextMove(char[][] board, Move opponentMove) {
        return getNextMove(board);
    }

    public static boolean isGameOver(char[][] board, boolean isWhite) {

        int blackCount = 0;
        int whiteCount = 0;

        for (int i = 0; i < 8; ++i) {
            for (int k = 0; k < 8; ++k) {
                if (board[k][i] >= 'a' && board[k][i] <= 'z') {
                    whiteCount++;
                }

                if (board[k][i] >= 'A' && board[k][i] <= 'Z') {
                    blackCount++;
                }
            }
        }

        return (whiteCount == 0 || blackCount == 0);
    }

    public static int evaluate(char[][] board) {

        int eval = 0;
        for (int i = 0; i < 8; ++i) {
            for (int k = 0; k < 8; ++k) {
                char c = board[k][i];
                switch (c) {
                    case 'k':
                        eval += 200;
                        break;
                    case 'r':
                        eval += 5;
                        break;
                    case 'b':
                    case 'n':
                        eval += 3;
                        break;
                    case 'q':
                        eval += 9;
                        break;
                    case 'p':
                        eval += 1;
                        break;
                    case 'K':
                        eval -= 200;
                        break;
                    case 'R':
                        eval -= 5;
                        break;
                    case 'B':
                    case 'N':
                        eval -= 3;
                        break;
                    case 'Q':
                        eval -= 9;
                        break;
                    case 'P':
                        eval -= 1;
                        break;
                    default:
                        break;
                }
            }
        }

        return eval;
    }

    public static ChessPieceType getChessPieceType(char[][] board, int offset) {
        ChessPieceType chessPieceType = ChessPieceType.NONE;
        char c = board[offset / 8][offset % 8];
        switch (c) {
            case 'k':
                chessPieceType = ChessPieceType.WHITE_KING;
                break;
            case 'r':
                chessPieceType = ChessPieceType.WHITE_ROOK;
                break;
            case 'b':
                chessPieceType = ChessPieceType.WHITE_BISHOP;
                break;
            case 'q':
                chessPieceType = ChessPieceType.WHITE_QUEEN;
                break;
            case 'n':
                chessPieceType = ChessPieceType.WHITE_KNIGHT;
                break;
            case 'p':
                chessPieceType = ChessPieceType.WHITE_PAWN;
                break;
            case 'K':
                chessPieceType = ChessPieceType.BLACK_KING;
                break;
            case 'R':
                chessPieceType = ChessPieceType.BLACK_ROOK;
                break;
            case 'B':
                chessPieceType = ChessPieceType.BLACK_BISHOP;
                break;
            case 'Q':
                chessPieceType = ChessPieceType.BLACK_QUEEN;
                break;
            case 'N':
                chessPieceType = ChessPieceType.BLACK_KNIGHT;
                break;
            case 'P':
                chessPieceType = ChessPieceType.BLACK_PAWN;
                break;
            default:
                break;
        }

        return chessPieceType;
    }


    public Wrapper minimax(char[][] board, int depth, boolean maximizingPlayer) {

        //long end = System.nanoTime();
        //long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (isGameOver(board, isWhite())) {
            Wrapper wrapper = new Wrapper(evaluate(board), null, 1);
            return wrapper;
        }

        if (depth == 0) {
            Wrapper wrapper = new Wrapper(evaluate(board), null, 2);
            return wrapper;
        }

        ArrayList<Move> moves = getNextMovesBitmapVer(board, maximizingPlayer);
        if (moves.size() == 0) {
            Wrapper wrapper = new Wrapper(evaluate(board), null, 3);
            return wrapper;
        }

        Move bestMove = moves.get(0);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : moves) {
                // make move
                int offsetFrom = move.fromY * 8 + move.fromX;
                int offsetTo = move.toY * 8 + move.toX;

                char c1 = board[move.fromY][move.fromX];
                char c2 = board[move.toX][move.toY];

                board[move.fromY][move.fromX] = 0;
                board[move.toX][move.toY] = c1;

                //if (offsetTo == a1) {
                    // undo
                    //board[move.fromY][move.fromX] = c1;
                    //board[move.toX][move.toY] = c2;
                    //continue;
                //}

                Wrapper wrapper = minimax(board, depth - 1, false);
                // undo move
                board[move.fromY][move.fromX] = c1;
                board[move.toY][move.toX] = c2;

                int currentEval = wrapper.eval;

                if (currentEval > maxEval) {
                    maxEval = currentEval;
                    bestMove = move;
                }
            }

            return new Wrapper(maxEval, bestMove);
        }

        int minEval = Integer.MAX_VALUE;
        for (Move move : moves) {
            // make move
            int offsetFrom = move.fromY * 8 + move.fromX;
            int offsetTo = move.toY * 8 + move.toX;

            char c1 = board[move.fromY][move.fromX];
            char c2 = board[move.toY][move.toX];

            board[move.fromY][move.fromX] = 0;
            board[move.toY][move.toX] = c1;

            //if (offsetTo == a2) {
             //   board[move.fromY][move.fromX] = c1;
              //  board[move.toY][move.toX] = c2;
               // continue;
            //}

            Wrapper wrapper = minimax(board, depth - 1, true);
            // undo move
            board[move.fromY][move.fromX] = c1;
            board[move.toY][move.toX] = c2;

            int currentEval = wrapper.eval;

            if (currentEval < minEval) {
                minEval = currentEval;
                bestMove = move;
            }
        }
        return new Wrapper(minEval, bestMove);
    }

    public static ArrayList<Move> getNextMovesBitmapVer(char[][] board, boolean isWhite) {

        ArrayList<Move> result = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < 64; ++i) {

            if (count == 16) {
                break;
            }

            ChessPieceType chessPieceType = getChessPieceType(board, i);
            Color color = Color.chessPieceColor(chessPieceType);

            if (chessPieceType == ChessPieceType.NONE || isWhite && color == Color.BLACK || !isWhite && color == Color.WHITE) {
                continue;
            }

            ++count;
            movesBitmapVersion(i, board, chessPieceType, isWhite, result);
        }

        return result;
    }

    public static void movesBitmapVersion(final int offset, char[][] board, final ChessPieceType chessPieceType, final boolean isWhite, ArrayList<Move> result) {
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

                ChessPieceType c1 = getChessPieceType(board, offsetAfterMove);
                Color c1Color = Color.chessPieceColor(c1);

                if (isWhite && c1Color == Color.WHITE || !isWhite && c1Color == Color.BLACK) {
                    break;
                }

                result.add(new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8));

                if (c1Color != Color.NONE || loopOnce) {
                    break;
                }
            }
        }
    }

    private static void pawnMovesBitmapVersion(final int offset, char[][] board, boolean isWhite, ArrayList<Move> result) {
        for (int i = 0; i < PAWN_MOVE_OFFSET.length; ++i) {
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_MOVE_OFFSET[i];
            int y = offset / 8;

            if ((i == 1 && y != (isWhite ? 6 : 1)) || offsetAfterMove < 0 || offsetAfterMove >= 64 || board[offsetAfterMove / 8][offsetAfterMove % 8] != 0) {
                break;
            }

            result.add(new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8));
        }
    }

    private static void pawnAttacksBitmapVersion(final int offset, char[][] board, boolean isWhite, ArrayList<Move> result) {
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

            result.add(new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8));

            if (c1 != ChessPieceType.NONE) {
                break;
            }
        }
    }
}
