package dominoes;

import java.util.ArrayList;
import java.util.Arrays;

public class Tree {
    ArrayList<Node> nodes;
    int maxPipsPerEnd;

    Tree(int maxPipsPerEnd) {
        this.maxPipsPerEnd = maxPipsPerEnd;
        this.nodes = generateNodes();
    }

    public ArrayList<Node> generateNodes() {
        ArrayList<Domino> dominoes = Domino.generateDominoes(this.maxPipsPerEnd);
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < dominoes.size(); i++) {
            for (int j = i + 1; j < dominoes.size(); j++) {
                Hand nodeHand = new Hand(maxPipsPerEnd);
                nodeHand.addDominoes(Arrays.asList(dominoes.get(i), dominoes.get(j)));
                nodes.add(new Node(nodeHand));
            }
        }
        return nodes;
    }
}
