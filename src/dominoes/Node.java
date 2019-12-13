package dominoes;

import java.util.ArrayList;

import static dominoes.Domino.generateDominoes;

public class Node {
    Hand hand;
    ArrayList<Node> children;

    Node(Hand hand) {
        this.hand = new Hand(6);
        this.hand.addDominoes(hand.dominoes);
        this.children = new ArrayList<>();
    }

    Node (Hand hand, Domino domino) {
        this.hand = new Hand(6);
        this.hand.addDominoes(hand.dominoes, domino);
        this.children = new ArrayList<>();
    }

    public void addAllChildren() {
        ArrayList<Domino> dominoes = generateDominoes(this.hand.maxPipsPerEnd);
        for (Domino domino : dominoes) {
            boolean anySameDomino = false;
            for (Domino dominoInHand : this.hand.dominoes) {
                if (domino.equals(dominoInHand)) {
                    anySameDomino = true; break;
                }
            }
            if (!anySameDomino) this.children.add(new Node(this.hand, domino));
        }
    }
}
