package academy.pocu.comp3500.assignment3;

public class ChessPiece {
    private int offset;
    private boolean isDisabled;

    public ChessPiece(int offset) {
        this.offset = offset;
        this.isDisabled = false;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isDisabled() {
        return isDisabled;
    }
}
