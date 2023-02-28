
package academy.pocu.comp3500.lab7;

import java.util.ArrayList;

public class Node {

    public Node() {
    }

    public Node(char letter) {
        this.letter = letter;
    }

    public boolean isEndLetter;
    public ArrayList<Integer> codeWordsIndex = new ArrayList<>();

    public char letter;
    public ArrayList<Node> nextNodes = new ArrayList<>(16);

    public void setCode(int index) {
        isEndLetter = true;
        codeWordsIndex.add(index);
    }

    public Node add(char c) {
        for (int i = 0; i < nextNodes.size(); ++i) {
            if (nextNodes.get(i).letter == ((int) c | 0x20)) {
                return nextNodes.get(i);
            }
        }

        nextNodes.add(new Node((char) (c | 0x20)));
        return nextNodes.get(nextNodes.size() - 1);
    }

    public Node getSubNodeOrNull(char c) {
        for (Node n : nextNodes) {
            if (n.letter == (0x20 | c)) {
                return n;
            }
        }

        return null;
    }

    public boolean isEndLetter() {
        return isEndLetter;
    }

    public ArrayList<Integer> getCodeWordsIndex() {
        return codeWordsIndex;
    }
}
