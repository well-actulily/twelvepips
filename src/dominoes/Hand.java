package dominoes;

import java.util.ArrayList;

public class Hand {
    int maxPipsPerEnd;
    ArrayList<Domino> dominoes;
    ArrayList<Integer> scores;
    int bestScore;

    public Hand(int maxPipsPerEnd) {
        this.maxPipsPerEnd = maxPipsPerEnd;
        this.dominoes = new ArrayList<>();
        this.scores = new ArrayList<>();
        this.scores.add(0);
    }

    public void addDomino(Domino domino) {
        this.dominoes.add(domino);
        calculateHandScores();
    }

    public void calculateHandScores() {
        int[] pipsCounts = getPipsCounts();
        ArrayList<Integer> newScores = new ArrayList<>();
        newScores.add(0);
        for (int i = 0; i <= maxPipsPerEnd; i++) {
            ArrayList<Integer> updateScores = new ArrayList<>();
            for (int j = 1; j <= pipsCounts[i]; j++) {
                for (Integer score : newScores) {
                    updateScores.add(score + i * j);
                }
            }
            if (newScores.size() <= updateScores.size()) newScores = updateScores;
        }
        this.scores = newScores;
        this.bestScore = getBestScore();
    }

    public int getBestScore() {
        int bestScore = Integer.MAX_VALUE;
        for (int score : scores) {
            if (score <= 12 && score > bestScore) bestScore = score;
            else if (score < bestScore) bestScore = score;
        }
        return bestScore;
    }

    public int[] getPipsCounts() {
        int[] pipsCounts = new int[maxPipsPerEnd + 1];
        for (Domino domino : dominoes) {
            pipsCounts[domino.end0Pips] += 1;
            pipsCounts[domino.end1Pips] += 1;
        }
        return pipsCounts;
    }


}
