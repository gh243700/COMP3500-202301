package academy.pocu.comp3500.assignment2;

import academy.pocu.comp3500.assignment2.datastructure.ArrayList;
import java.io.BufferedWriter;
import java.io.IOException;

public final class Indent {
    private String message;
    private final ArrayList<Indent> subIndents = new ArrayList<>();
    private Indent parent;

    public Indent() {
    }

    private Indent(String message, Indent parent) {
        this.message = message;
        this.parent = parent;
    }

    public Indent registerSubIndent(final String message) {
        Indent subIndent = new Indent(message, this);
        subIndents.add(subIndent);

        return subIndent;
    }

    public Indent getParent() {
        return parent;
    }

    public void discard() {
        for (Indent indent : subIndents) {
            indent.parent = null;
            indent.discard();
        }

        subIndents.clear();
    }

    public void printAllRecursive(final BufferedWriter writer, final int level, final String filter) {
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
                subIndent.printAllRecursive(writer, level + 1, filter);
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