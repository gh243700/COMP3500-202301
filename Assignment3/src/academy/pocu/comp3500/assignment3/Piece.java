package academy.pocu.comp3500.assignment3;

public class Piece implements Comparable<Piece> {
    public char pieceName;
    public int xPos;
    public int yPos;
    public int importance;

    public Piece(char pieceName, int xPos, int yPos) {
        this.pieceName = pieceName;
        this.xPos = xPos;
        this.yPos = yPos;

        switch (pieceName) {
            case 'k':
            case 'K':
                importance = 900;
            case 'q':
            case 'Q':
                importance = 90;
            case 'b':
            case 'B':
                importance = 30;
            case 'n':
            case 'N':
                importance = 30;
            case 'r':
            case 'R':
                importance = 50;
            case 'p':
            case 'P':
                importance = 10;
        }
    }

    @Override
    public int compareTo(Piece otherPieceInfo) {
        if (this.importance > otherPieceInfo.importance) {
            return 1;
        } else {
            return -1;
        }
    }
}