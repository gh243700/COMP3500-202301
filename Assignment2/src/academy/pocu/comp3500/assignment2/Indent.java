package academy.pocu.comp3500.assignment2;

import academy.pocu.comp3500.assignment2.datastructure.LinkedList;
import java.io.BufferedWriter;
import java.io.IOException;

public final class Indent {
    private String message;
    private final LinkedList<Indent> subIndents = new LinkedList<>();
    private Indent parent;

    public Indent() {
    }

    public Indent(String message, Indent parent) {
        this.message = message;
        this.parent = parent;
    }

    public Indent getLastSubIndent() {
        if (subIndents.getSize() == 0) {
            return null;
        }

        return subIndents.getLast();
    }

    public Indent addSubIndent(String message) {
        Indent subIndent = new Indent(message, this);
        subIndents.add(subIndent);

        return subIndent;
    }

    public Indent getParent() {
        return parent;
    }

    private void forgetParent() {
        this.parent = null;
    }

    public void discard() {
        for (Indent indent : subIndents) {
            indent.forgetParent();
            indent.discard();
        }

        subIndents.clear();
    }

    public void printAll(final BufferedWriter writer, int level, String filter) {
        if (filter != null && level != 0 && message != null) {
            if (!message.contains(filter)) {
                return;
            }
        }

        try {
            if (message != null) {
                for (int k = 0; k < level - 1; ++k) {
                    writer.write("  ");
                }

                writer.write(message);
                writer.newLine();
            }

            for (Indent subIndent : subIndents) {
                subIndent.printAll(writer, level + 1, filter);
            }

            if (level == 0) {
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}