package academy.pocu.comp3500.lab7;

import java.util.ArrayList;

public class Decryptor {
    private Node root = new Node();
    private final String[] codeWords;

    public Decryptor(final String[] codeWords) {
        this.codeWords = new String[codeWords.length];

        for (int i = 0; i < codeWords.length; ++i) {

            char[] str = new char[codeWords[i].length()];
            char[] copy = new char[codeWords[i].length()];
            for (int j = 0; j < codeWords[i].length(); ++j) {
                str[j] = (char) ((0x20 | (int) codeWords[i].charAt(j)));
                copy[j] = str[j];
            }

            for (int j = 0; j < str.length; ++j) {
                for (int k = 0; k < str.length - j - 1; ++k) {
                    if (str[k] > str[k + 1]) {
                        char temp = str[k];
                        str[k] = str[k + 1];
                        str[k + 1] = temp;
                    }
                }
            }

            Node node = root;
            StringBuilder stringBuilder = new StringBuilder(str.length);
            for (int j = 0; j < str.length; ++j) {
                node = node.add(str[j]);
                stringBuilder.append(copy[j]);
            }
            node.setCode(i);


            this.codeWords[i] = stringBuilder.toString();
        }
    }

    public String[] findCandidates(final String word) {
        char[] str = new char[word.length()];

        for (int i = 0; i < word.length(); ++i) {
            str[i] = (char) ((int) word.charAt(i) | 0x20);
        }

        for (int i = 0; i < str.length; ++i) {
            for (int k = 0; k < str.length - i - 1; ++k) {
                if (str[k] > str[k + 1]) {
                    char temp = str[k];
                    str[k] = str[k + 1];
                    str[k + 1] = temp;
                }
            }
        }

        Node node = root;
        for (int i = 0; i < str.length && node != null; ++i) {
            node = node.getSubNodeOrNull(str[i]);
        }

        if (node == null || node.isEndLetter() == false) {
            return new String[0];
        }

        ArrayList<Integer> index = node.getCodeWordsIndex();
        String[] result = new String[index.size()];

        for (int i = 0; i < result.length; ++i) {
            result[i] = codeWords[index.get(i)];
        }

        return result;
    }
}