package academy.pocu.comp3500.assignment3;

import academy.pocu.comp3500.assignment3.chess.Move;
import academy.pocu.comp3500.assignment3.chess.PlayerBase;

import java.util.HashMap;
import java.util.LinkedList;
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

    private int miniMaxDepth = 4;
    private final static int BOARD_SIZE = 8;
    private LinkedList<Piece> whitePieceList;
    private LinkedList<Piece> blackPieceList;
    private HashMap<Character, Integer> piecePointHashMap;
    private static final int[][] KING_MOVE_OFFSETS = {
            {-1, 1},
            {-1, 0},
            {-1, -1},
            {0, 1},
            {0, -1},
            {1, 1},
            {1, 0},
            {1, -1}
    };

    private static final int[][] KNIGHT_MOVE_OFFSETS = {
            {-2, -1},
            {-2, 1},
            {-1, -2},
            {-1, 2},
            {1, -2},
            {1, 2},
            {2, -1},
            {2, 1}
    };


    public Player(boolean isWhite, int maxMoveTimeMilliseconds) {
        super(isWhite, maxMoveTimeMilliseconds);
        whitePieceList = new LinkedList<Piece>();
        blackPieceList = new LinkedList<Piece>();
        piecePointHashMap = new HashMap<Character, Integer>();

        piecePointHashMap.put('K', 900);
        piecePointHashMap.put('Q', 90);
        piecePointHashMap.put('R', 50);
        piecePointHashMap.put('B', 30);
        piecePointHashMap.put('N', 30);
        piecePointHashMap.put('P', 10);
    }

    @Override
    public Move getNextMove(char[][] board) {
        for (int y = 0; y < BOARD_SIZE; ++y)
            for (int x = 0; x < BOARD_SIZE; ++x) {
                if (board[y][x] == 0) continue;
                else if (Character.isLowerCase(board[y][x])) {
                    whitePieceList.add(new Piece(board[y][x], x, y));
                } else {
                    blackPieceList.add(new Piece(board[y][x], x, y));
                }
            }

        Move nextMove = getBestMoveRecursive(board, 0, true).move;
        moveUpdate(nextMove, isWhite());
        return nextMove;
    }

    @Override
    public Move getNextMove(char[][] board, Move opponentMove) {
        if (whitePieceList.size() == 0 || blackPieceList.size() == 0) {
            for (int y = 0; y < BOARD_SIZE; ++y)
                for (int x = 0; x < BOARD_SIZE; ++x) {
                    if (board[y][x] == 0) continue;
                    else if (Character.isLowerCase(board[y][x])) {
                        whitePieceList.add(new Piece(board[y][x], x, y));
                    } else {
                        blackPieceList.add(new Piece(board[y][x], x, y));
                    }
                }
        }

        moveUpdate(opponentMove, !isWhite());
        Move nextMove = getBestMoveRecursive(board, 0, true).move;
        moveUpdate(nextMove, isWhite());
        return nextMove;
    }

    private void moveUpdate(Move move, boolean isWhite) {
        if (isWhite) {
            for (int i = 0; i < whitePieceList.size(); ++i) {
                if (whitePieceList.get(i).xPos == move.fromX && whitePieceList.get(i).yPos == move.fromY) {
                    whitePieceList.get(i).xPos = move.toX;
                    whitePieceList.get(i).yPos = move.toY;
                    break;
                }
            }
            for (int i = 0; i < blackPieceList.size(); ++i) {
                if (blackPieceList.get(i).xPos == move.toX && blackPieceList.get(i).yPos == move.toY) {
                    blackPieceList.remove(i);
                }
            }
        } else {
            for (int i = 0; i < blackPieceList.size(); ++i) {
                if (blackPieceList.get(i).xPos == move.fromX && blackPieceList.get(i).yPos == move.fromY) {
                    blackPieceList.get(i).xPos = move.toX;
                    blackPieceList.get(i).yPos = move.toY;
                    break;
                }
            }
            for (int i = 0; i < whitePieceList.size(); ++i) {
                if (whitePieceList.get(i).xPos == move.toX && whitePieceList.get(i).yPos == move.toY) {
                    whitePieceList.remove(i);
                }
            }
        }
    }

    private void printPieceInfo() {
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < whitePieceList.size(); ++i) {
            board[whitePieceList.get(i).yPos][whitePieceList.get(i).xPos] = whitePieceList.get(i).pieceName;
        }
        for (int i = 0; i < blackPieceList.size(); ++i) {
            board[blackPieceList.get(i).yPos][blackPieceList.get(i).xPos] = blackPieceList.get(i).pieceName;
        }

        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                System.out.print(board[y][x]);
            }
            System.out.println();
        }
    }

    private Evaluation getBestMoveRecursive(char[][] board, int currentDepth, boolean myTurn) {
        // 깊이에 도달했을 경우 마감
        if (currentDepth == miniMaxDepth)
            return new Evaluation(0, null);

        // 이번에 움직일 수 있는 말 리스트 찾기
        LinkedList<Piece> movablePieceList;
        if (myTurn) {
            if (this.isWhite())
                movablePieceList = whitePieceList;
            else
                movablePieceList = blackPieceList;
        } else {
            if (this.isWhite())
                movablePieceList = blackPieceList;
            else
                movablePieceList = whitePieceList;
        }

        // 보드 순회하면서 각 말이 해당 지점에 올 수 있는지 확인하고 그 말이 해당 지점에 왔을 때 점수 체크하고 저장
        var evaluationList = new LinkedList<Evaluation>();
        var tempMove = new Move();
        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                Piece bestMovablePiece = null;
                for (var movablePiece : movablePieceList) {
                    // 기물마다 해당 좌표 이동 가능 체크
                    tempMove.fromX = movablePiece.xPos;
                    tempMove.fromY = movablePiece.yPos;
                    tempMove.toX = x;
                    tempMove.toY = y;

                    if (!isMoveValid(board, movablePiece, tempMove)) continue;

                    // 기존 이동 후보 기물의 움직임과 비교해 비교적 중요도가 낮은 애가 이동할 것이라 예측하고 갱신
                    if (bestMovablePiece == null) {
                        bestMovablePiece = movablePiece;
                    } else {
                        if (bestMovablePiece.importance == movablePiece.importance) {
                            if (Math.abs(bestMovablePiece.xPos - 4) > Math.abs(movablePiece.xPos - 4))
                                bestMovablePiece = movablePiece;
                        } else if (bestMovablePiece.importance > movablePiece.importance) {
                            bestMovablePiece = movablePiece;
                        }
                    }
                }

                // 해당 위치로 움직일 수 있는 기물이 없으면 스킵
                if (bestMovablePiece == null) continue;

                // 다음 재귀를 위한 보드 복사
                var newBoard = copyBoard(board);

                // 잡을 상대 말이 있다면 다음 깊이로 진행하기 이전에 제거 처리 후 점수 기록
                Piece caughtPiece = null;
                int evaluatedScore = 0;
                if (newBoard[y][x] != 0) {
                    if (myTurn) {
                        if (isWhite()) {
                            if (Character.isUpperCase(newBoard[y][x])) {
                                for (int i = 0; i < blackPieceList.size(); ++i) {
                                    if (blackPieceList.get(i).xPos == x && blackPieceList.get(i).yPos == y) {
                                        evaluatedScore += piecePointHashMap.get(Character.toUpperCase(blackPieceList.get(i).pieceName)) - bestMovablePiece.importance;
                                        caughtPiece = blackPieceList.remove(i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            if (Character.isLowerCase(newBoard[y][x])) {
                                for (int i = 0; i < whitePieceList.size(); ++i) {
                                    if (whitePieceList.get(i).xPos == x && whitePieceList.get(i).yPos == y) {
                                        evaluatedScore += piecePointHashMap.get(Character.toUpperCase(whitePieceList.get(i).pieceName)) - bestMovablePiece.importance;
                                        caughtPiece = whitePieceList.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        if (isWhite()) {
                            if (Character.isLowerCase(newBoard[y][x])) {
                                for (int i = 0; i < whitePieceList.size(); ++i) {
                                    if (whitePieceList.get(i).xPos == x && whitePieceList.get(i).yPos == y) {
                                        evaluatedScore -= piecePointHashMap.get(Character.toUpperCase(whitePieceList.get(i).pieceName)) - bestMovablePiece.importance;
                                        caughtPiece = whitePieceList.remove(i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            if (Character.isUpperCase(newBoard[y][x])) {
                                for (int i = 0; i < blackPieceList.size(); ++i) {
                                    if (blackPieceList.get(i).xPos == x && blackPieceList.get(i).yPos == y) {
                                        evaluatedScore -= piecePointHashMap.get(Character.toUpperCase(blackPieceList.get(i).pieceName)) - bestMovablePiece.importance;
                                        caughtPiece = blackPieceList.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                // 이동 실행. 이동하기 이전 위치 저장
                var fromMovablePieceXPos = bestMovablePiece.xPos;
                var fromMovablePieceYPos = bestMovablePiece.yPos;
                newBoard[fromMovablePieceYPos][fromMovablePieceXPos] = 0;
                newBoard[y][x] = bestMovablePiece.pieceName;
                bestMovablePiece.xPos = x;
                bestMovablePiece.yPos = y;

                // 다음 깊이 재귀하면서 해당 트리의 점수 겟
                var totalScore = getBestMoveRecursive(newBoard, currentDepth + 1, !myTurn).score + evaluatedScore;

                // 평가 저장
                var evaluation = new Evaluation(totalScore, new Move(fromMovablePieceXPos, fromMovablePieceYPos, x, y));
                evaluationList.add(evaluation);

                // 잡았던 말은 다음 탐색을 위해 복구
                if (caughtPiece != null) {
                    if (Character.isLowerCase(caughtPiece.pieceName))
                        whitePieceList.add(caughtPiece);
                    else
                        blackPieceList.add(caughtPiece);
                }

                bestMovablePiece.xPos = fromMovablePieceXPos;
                bestMovablePiece.yPos = fromMovablePieceYPos;
            }
        }

        // 기록된 점수 중 베스트 점수 or 워스트 점수 리턴
        if (evaluationList.size() == 0)
            return new Evaluation(0, null);

        var resultEvaluation = evaluationList.get(0);
        for (int i = 1; i < evaluationList.size(); ++i) {
            if (myTurn) {
                if (resultEvaluation.score < evaluationList.get(i).score)
                    resultEvaluation = evaluationList.get(i);
            } else {
                if (resultEvaluation.score > evaluationList.get(i).score)
                    resultEvaluation = evaluationList.get(i);
            }
        }

        return resultEvaluation;
    }

    private boolean isMyPiece(char piece) {
        if (isWhite())
            return Character.isLowerCase(piece);
        else
            return Character.isUpperCase(piece);
    }

    private boolean isOpponentPiece(char piece) {
        if (isWhite())
            return Character.isUpperCase(piece);
        else
            return Character.isLowerCase(piece);
    }

    private static char[][] copyBoard(final char[][] board) {
        char[][] newBoard = new char[BOARD_SIZE][BOARD_SIZE];

        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                newBoard[y][x] = board[y][x];
            }
        }

        return newBoard;
    }

    private static boolean isMoveValid(char[][] board, Piece pieceInfo, Move move) {
        switch (pieceInfo.pieceName) {
            case 'k':
            case 'K':
                return isKingMoveValid(board, move);
            case 'q':
            case 'Q':
                return isQueenMoveValid(board, move);
            case 'b':
            case 'B':
                return isBishopMoveValid(board, move);
            case 'n':
            case 'N':
                return isKnightMoveValid(board, move);
            case 'r':
            case 'R':
                return isRookMoveValid(board, move);
            case 'p':
            case 'P':
                return isPawnMoveValid(board, move);
        }
        return false;
    }

    private static boolean isBishopMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

//        System.out.println(fromPiece + " , " + toPiece);

        if (toPiece != 0 && Character.isLowerCase(fromPiece) == Character.isLowerCase(toPiece)) {
            return false;
        }

        if (Math.abs(move.fromX - move.toX) != Math.abs(move.fromY - move.toY)) {
            return false;
        }

        int xIncrement = move.fromX < move.toX ? 1 : -1;
        int yIncrement = move.fromY < move.toY ? 1 : -1;

        int x = move.fromX + xIncrement;
        int y = move.fromY + yIncrement;

        while (x != move.toX && y != move.toY) {
            if (board[y][x] != 0 && x != move.toX && y != move.toY) {
                return false;
            }

            x += xIncrement;
            y += yIncrement;
        }

        return true;
    }

    private static boolean isRookMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        if (toPiece != 0 && Character.isLowerCase(fromPiece) == Character.isLowerCase(toPiece)) {
            return false;
        }

        if (move.fromX == move.toX) {
            int yIncrement = move.fromY < move.toY ? 1 : -1;

            int y = move.fromY + yIncrement;

            while (y != move.toY) {
                if (board[y][move.fromX] != 0) {
                    return false;
                }

                y += yIncrement;
            }

            return true;

        } else if (move.fromY == move.toY) {
            int xIncrement = move.fromX < move.toX ? 1 : -1;

            int x = move.fromX + xIncrement;

            while (x != move.toX) {
                if (board[move.fromY][x] != 0) {
                    return false;
                }

                x += xIncrement;
            }

            return true;
        }

        return false;
    }

    private static boolean isKnightMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        if (toPiece != 0 && Character.isLowerCase(fromPiece) == Character.isLowerCase(toPiece)) {
            return false;
        }

        for (int i = 0; i < KNIGHT_MOVE_OFFSETS.length; ++i) {
            if (move.fromX + KNIGHT_MOVE_OFFSETS[i][0] == move.toX && move.fromY + KNIGHT_MOVE_OFFSETS[i][1] == move.toY) {
                return true;
            }
        }

        return false;
    }

    private static boolean isQueenMoveValid(char[][] board, Move move) {
        return isBishopMoveValid(board, move) || isRookMoveValid(board, move);
    }

    private static boolean isKingMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        if (toPiece != 0 && Character.isLowerCase(fromPiece) == Character.isLowerCase(toPiece)) {
            return false;
        }

        for (int i = 0; i < KING_MOVE_OFFSETS.length; ++i) {
            if (move.fromX + KING_MOVE_OFFSETS[i][0] == move.toX && move.fromY + KING_MOVE_OFFSETS[i][1] == move.toY) {
                return true;
            }
        }

        return false;
    }

    private static boolean isPawnMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        boolean isFromPieceWhite = Character.isLowerCase(fromPiece);
        boolean isToPieceWhite = Character.isLowerCase(toPiece);

        if (toPiece != 0 && isFromPieceWhite == isToPieceWhite) {
            return false;
        }

        if (toPiece != 0 && move.fromX == move.toX) {
            return false;
        }

        boolean hasMoved = isFromPieceWhite ? move.fromY != 6 : move.fromY != 1;

        if (!hasMoved && move.fromX == move.toX && Math.abs(move.toY - move.fromY) == 2) {
            if (move.toY > move.fromY && !isFromPieceWhite && board[move.toY - 1][move.toX] == 0) {
                return true;
            }

            return move.toY < move.fromY && isFromPieceWhite && board[move.toY + 1][move.toX] == 0;
        } else if (move.fromX == move.toX && Math.abs(move.toY - move.fromY) == 1) {
            if (move.toY > move.fromY && !isFromPieceWhite) {
                return true;
            }

            return move.toY < move.fromY && isFromPieceWhite;
        } else if (move.toX == move.fromX - 1 || move.toX == move.fromX + 1) {
            if (toPiece != 0 && isToPieceWhite != isFromPieceWhite) {
                return isFromPieceWhite ? move.toY == move.fromY - 1 : move.toY == move.fromY + 1;
            }
        }

        return false;
    }
    //----------------------

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

    public Move prioritizeProtectingOwnPiece(char[][] board, Move m1, Move m2, int[] values) {

        char t1 = board[m1.fromY][m1.fromX];
        char t2 = board[m1.toY][m1.toX];

        board[m1.fromY][m1.fromX] = 0;
        board[m1.toY][m1.toX] = t1;
        if (t2 != 0) {
            if (isWhite()) {
                values[1] -= VALUES[getChessPieceType(t2).ordinal()];
                --values[3];
            } else {
                values[0] -= VALUES[getChessPieceType(t2).ordinal()];
                --values[2];
            }
        }

        int eval1 = minimax(board, 1, false, (isWhite() ? false : true), -1, new Move[1], values, (char) 0);

        board[m1.fromY][m1.fromX] = t1;
        board[m1.toY][m1.toX] = t2;
        if (t2 != 0) {
            if (isWhite()) {
                values[1] += VALUES[getChessPieceType(t2).ordinal()];
                ++values[3];
            } else {
                values[0] += VALUES[getChessPieceType(t2).ordinal()];
                ++values[2];
            }
        }


        t1 = board[m2.fromY][m2.fromX];
        t2 = board[m2.toY][m2.toX];

        board[m2.fromY][m2.fromX] = 0;
        board[m2.toY][m2.toX] = t1;
        if (t2 != 0) {
            if (isWhite()) {
                values[1] -= VALUES[getChessPieceType(t2).ordinal()];
                --values[3];
            } else {
                values[0] -= VALUES[getChessPieceType(t2).ordinal()];
                --values[2];
            }
        }


        int eval2 = minimax(board, 1, false, (isWhite() ? false : true), -1, new Move[1], values, (char) 0);

        board[m2.fromY][m2.fromX] = t1;
        board[m2.toY][m2.toX] = t2;
        if (t2 != 0) {
            if (isWhite()) {
                values[1] += VALUES[getChessPieceType(t2).ordinal()];
                ++values[3];
            } else {
                values[0] += VALUES[getChessPieceType(t2).ordinal()];
                ++values[2];
            }
        }

        return (eval1 >= eval2) ? m1 : m2;
    }


    public int minimax(char[][] board, int depth, boolean maximizingPlayer, boolean isWhite, long start, Move[] finalResult, int[] values, char t2) {
        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        if (start != -1 && duration >= getMaxMoveTimeMilliseconds()) {
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


            noResult = movesBitmapVersion(board, index, chessPieceType, depth, maximizingPlayer, isWhite, start, finalResult, maxEval, values);
            ++count;
        }

        if (noResult) {
            return evaluate(values);
        }

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

                board[offset / 8][offset % 8] = 0;
                board[offsetAfterMove / 8][offsetAfterMove % 8] = t1;

                if (t2 != 0) {
                    if (isWhite) {
                        values[1] -= VALUES[getChessPieceType(t2).ordinal()];
                        --values[3];
                    } else {
                        values[0] -= VALUES[getChessPieceType(t2).ordinal()];
                        --values[2];
                    }
                }


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
                    int eval = evaluate(values);
                    if (maximizingPlayer ? eval > currentEval : eval < currentEval) {
                        maxEval[0] = eval;
                        if (isTopDepth) {
                            finalResult[0] = move;
                        }
                    } else if (maxEval[0] == Integer.MIN_VALUE + 1 || maxEval[0] == Integer.MAX_VALUE) {
                        maxEval[0] = currentEval;
                        if (isTopDepth) {
                            finalResult[0] = move;
                        }
                    }

                    //maxEval[0] = currentEval;
                    //if (isTopDepth) {
                    //    finalResult[0] = move;
                    //}
                } else if (maxEval[0] == currentEval) {
                    if (isTopDepth) {
                        finalResult[0] = prioritizeProtectingOwnPiece(board, finalResult[0], move, values);
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
                int eval = evaluate(values);
                if (maximizingPlayer ? eval > currentEval : eval < currentEval) {
                    maxEval[0] = eval;
                    if (isTopDepth) {
                        finalResult[0] = move;
                    }
                } else if (maxEval[0] == Integer.MIN_VALUE + 1 || maxEval[0] == Integer.MAX_VALUE) {
                    maxEval[0] = currentEval;
                    if (isTopDepth) {
                        finalResult[0] = move;
                    }
                }

                //maxEval[0] = currentEval;

            } else if (maxEval[0] == currentEval) {
                if (isTopDepth) {
                    if (finalResult[0] == null) {
                        System.out.println();
                    }
                    finalResult[0] = prioritizeProtectingOwnPiece(board, finalResult[0], move, values);
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
                int eval = evaluate(values);
                if (maximizingPlayer ? eval > currentEval : eval < currentEval) {
                    maxEval[0] = eval;
                    if (isTopDepth) {
                        finalResult[0] = move;
                    }
                } else if (maxEval[0] == Integer.MIN_VALUE + 1 || maxEval[0] == Integer.MAX_VALUE) {
                    maxEval[0] = currentEval;
                    if (isTopDepth) {
                        finalResult[0] = move;
                    }
                }


                //maxEval[0] = currentEval;
                //if (isTopDepth) {
                //    finalResult[0] = move;
                //}
            } else if (maxEval[0] == currentEval) {
                if (isTopDepth) {
                    finalResult[0] = prioritizeProtectingOwnPiece(board, finalResult[0], move, values);
                }
            }

            int eval = evaluate(values);
            if (maximizingPlayer ? eval > maxEval[0] : eval < maxEval[0]) {
                maxEval[0] = eval;
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