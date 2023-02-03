package academy.pocu.comp3500.assignment2;

import java.io.BufferedWriter;

public final class Logger {
    private static final Indent ROOT = new Indent();
    private static Indent current = ROOT;
    public static void log(final String text) {
        current.addSubIndent(text);
    }
    public static void printTo(final BufferedWriter writer) {
        printTo(writer, null);
    }
    public static void printTo(final BufferedWriter writer, final String filter) {
        ROOT.printAllRecursive(writer, 0, filter);
    }

    public static void clear() {
        ROOT.discard();
        current = ROOT;
    }

    public static Indent indent() {
        current.addSubIndent(null);
        current = current.getLastSubIndent();

        return current;
    }

    public static void unindent() {
        if (current.getParent() == null) {
            return;
        }

        current = current.getParent();
    }
}