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
        depth = maxMoveTimeMilliseconds / 1000 * 2;
    }

    @Override
    public Move getNextMove(char[][] board) {
        long start = System.nanoTime();
        Bitmap bitmap = Bitmap.convertToBitmap(board);
        Node node = new Node(bitmap, null, null);

        Node resultNode = minimax(node, depth, start, isWhite(), isWhite());

        int in = nodePool.allocCount;
        int out = nodePool.deleteCount;

        if (timeOut) {
            --depth;
        }

        return resultNode.getMove();
    }

    @Override
    public Move getNextMove(char[][] board, Move opponentMove) {
        return getNextMove(board);
    }

    public Node minimax(Node node, int depth, final long start, boolean maximizingPlayer, boolean isWhite) {
        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (duration >= getMaxMoveTimeMilliseconds()) {
            timeOut = true;
            node.setEvaluationValue(node.getBitmap().evaluate());
            return node;
        }

        if (depth <= 0) {
            timeOut = false;
            node.setEvaluationValue(node.getBitmap().evaluate());
            return node;
        }

        ArrayList<Node> nextPossibleMoves = getNextMovesBitmapVer(node.getBitmap(), maximizingPlayer, (depth == this.depth) ? null : node);

        if (nextPossibleMoves.size() == 0) {
            node.setEvaluationValue(node.getBitmap().evaluate());
            return node;
        }

        ArrayList<Node> moves = new ArrayList<>();

        for (int i = 0; i < nextPossibleMoves.size(); ++i) {
            Node nextNode = nextPossibleMoves.get(i);

            Node m = minimax(nextNode, depth - 1, start, !maximizingPlayer, isWhite);
            moves.add(m);
        }

        Node leafNode;

        if (maximizingPlayer) {
            leafNode = maxEvaluationValue(moves);
        } else {
            leafNode = minEvaluationValue(moves);
        }

        if (depth == this.depth) {
            return leafNode;
        }

        node.setEvaluationValue(leafNode.getEvaluationValue());
        return node;
    }

    private static Node maxEvaluationValue(ArrayList<Node> arrayList) {
        int value = Integer.MIN_VALUE;
        Node result = null;

        for (Node node : arrayList) {
            int evaluationValue = node.getEvaluationValue();
            if (value < evaluationValue) {
                if (result != null) {
                    //nodePool.delete(result);
                }
                result = node;
                value = evaluationValue;
            } else {
                //nodePool.delete(node);
            }
        }

        return result;
    }

    private static Node minEvaluationValue(ArrayList<Node> arrayList) {
        int value = Integer.MAX_VALUE;
        Node result = null;

        for (Node node : arrayList) {
            int evaluationValue = node.getEvaluationValue();

            if (value > evaluationValue) {
                if (result != null) {
                    //nodePool.delete(result);
                }

                result = node;
                value = evaluationValue;
            } else {
                //nodePool.delete(node);
            }
        }

        return result;
    }

    public static ArrayList<Node> getNextMovesBitmapVer(Bitmap board, boolean isWhite, Node parent) {

        ArrayList<Node> result = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < 64; ++i) {
            if (count == 16) {
                break;
            }

            ChessPieceType chessPieceType = board.getChessPieceType(i);

            if (isWhite && board.chessPieceColor(i) == Color.BLACK || !isWhite && board.chessPieceColor(i) == Color.WHITE || chessPieceType == ChessPieceType.NONE) {
                continue;
            }

            ++count;
            movesBitmapVersion(i, board, chessPieceType, isWhite, result, parent);
        }

        return result;
    }

    public static void movesBitmapVersion(final int offset, final Bitmap board, final ChessPieceType chessPieceType, final boolean isWhite, ArrayList<Node> result, Node parent) {
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
                pawnMovesBitmapVersion(offset, board, isWhite, result, parent);
                pawnAttacksBitmapVersion(offset, board, isWhite, result, parent);
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

                Bitmap boardCopy = board.makeCopy();
                boardCopy.setBitmap(offset, ChessPieceType.NONE);
                boardCopy.setBitmap(offsetAfterMove, chessPieceType);

                Node node = new Node(boardCopy, new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8), parent);
                node.setParent(parent == null ? node : parent);
                result.add(node);

                if (c1Color != Color.NONE || loopOnce) {
                    break;
                }
            }
        }
    }

    private static void pawnMovesBitmapVersion(final int offset, final Bitmap board, boolean isWhite, ArrayList<Node> result, Node parent) {
        for (int i = 0; i < PAWN_MOVE_OFFSET.length; ++i) {
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_MOVE_OFFSET[i];
            int y = offset / 8;

            if ((i == 1 && y != (isWhite ? 6 : 1)) || offsetAfterMove < 0 || offsetAfterMove >= 64 || board.chessPieceColor(offsetAfterMove) != Color.NONE) {
                break;
            }

            Bitmap boardCopy = board.makeCopy();

            boardCopy.setBitmap(offset, ChessPieceType.NONE);
            boardCopy.setBitmap(offsetAfterMove, (isWhite) ? ChessPieceType.WHITE_PAWN : ChessPieceType.BLACK_PAWN);

            Node node = new Node(boardCopy, new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8), parent);
            result.add(node);
        }
    }

    private static void pawnAttacksBitmapVersion(final int offset, final Bitmap board, boolean isWhite, ArrayList<Node> result, Node parent) {
        for (int i = 0; i < PAWN_ATTACK_OFFSET.length; ++i) {
            int x = 8 * (7 - offset % 8) + offset / 8;
            x += (isWhite ? -1 : 1) * PAWN_ATTACK_BOUND_X[i] * 8;
            int offsetAfterMove = offset + (isWhite ? -1 : 1) * PAWN_ATTACK_OFFSET[i];

            ChessPieceType c1 = board.getChessPieceType(offsetAfterMove);

            if (board.chessPieceColor(offsetAfterMove) != ((isWhite) ? Color.BLACK : Color.WHITE) || offsetAfterMove < 0 || offsetAfterMove >= 64 || x < 0 || x >= 64) {
                break;
            }

            Bitmap boardCopy = board.makeCopy();
            boardCopy.setBitmap(offset, ChessPieceType.NONE);
            boardCopy.setBitmap(offsetAfterMove, (isWhite) ? ChessPieceType.WHITE_PAWN : ChessPieceType.BLACK_PAWN);

            Node node = new Node(boardCopy, new Move(offset % 8, offset / 8, offsetAfterMove % 8, offsetAfterMove / 8), parent);

            result.add(node);

            if (c1 != ChessPieceType.NONE) {
                break;
            }
        }
    }
}
