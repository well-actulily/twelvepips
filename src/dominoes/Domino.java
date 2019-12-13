package dominoes;

import java.util.ArrayList;

public class Domino {
    public int end0Pips;
    public int end1Pips;

    public Domino(int end0Pips, int end1Pips) {
        this.end0Pips = end0Pips;
        this.end1Pips = end1Pips;
    }

    public boolean equals(Domino otherDomino) {
        return (this.end0Pips == otherDomino.end0Pips && this.end1Pips == otherDomino.end1Pips)
            || (this.end1Pips == otherDomino.end0Pips && this.end0Pips == otherDomino.end0Pips);
    }

    public static ArrayList<Domino> generateDominoes(int maxPipsPerEnd) {
        ArrayList<Domino> dominoes = new ArrayList<>();
        for (int i = 0; i <= maxPipsPerEnd; i++) {
            for (int j = i; j <= maxPipsPerEnd; j++) {
                dominoes.add(new Domino(i, j));
            }
        }
        return dominoes;
    }
}
