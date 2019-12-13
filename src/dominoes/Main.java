package dominoes;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        runSimulation();
    }

    public static void runSimulation () {
        int MAX_PIPS_PER_END = 6;
        int NUM_DECKS = 1;
        int DEALER_STAY = 11;
        int MAX_HAND_SIZE = 4;
        int TARGET_VALUE = 12;
        double TWO_BONES_PAY_RATIO = 1.5;

        double winnings = 0;
        for (int j = 1; j <= 10_000; j++) {
            for (int i = 0; i < 100_000; i++) {
                ArrayList<Domino> shoe = createShoe(MAX_PIPS_PER_END, NUM_DECKS);
                Hand dealerHand = new Hand(MAX_PIPS_PER_END);
                Hand playerHand = new Hand(MAX_PIPS_PER_END);
                dealDominoesToHand(playerHand, shoe, 2);
                dealDominoesToHand(dealerHand, shoe, 2);
                // Player scores 12 with only 'two bones.' If dealer has also done so, tie goes to the dealer.
                if (playerHand.bestScore == TARGET_VALUE) {
                    winnings += dealerHand.bestScore != TARGET_VALUE ? TWO_BONES_PAY_RATIO : -1;
                    continue;
                }
                // Player hits according to strategy function.
                while(playerHand.dominoes.size() < MAX_HAND_SIZE && playerStrategySaysToHit(playerHand, dealerHand)) {
                    dealDominoesToHand(playerHand, shoe, 1);
                }
                // Player busts.
                if (playerHand.bestScore > TARGET_VALUE) { winnings += -1; continue; }
                // Dealer hits until stand constant.
                while(dealerHand.bestScore < DEALER_STAY && dealerHand.dominoes.size() < MAX_HAND_SIZE) {
                    dealDominoesToHand(dealerHand, shoe, 1);
                }
                // Dealer busts.
                if (dealerHand.bestScore > TARGET_VALUE) { winnings += 1; continue; }
                // Player and dealer push.
                if (playerHand.bestScore == dealerHand.bestScore) { continue; }
                // If player has higher score, the player wins, otherwise the dealer does.
                winnings += playerHand.bestScore > dealerHand.bestScore ?  1 : -1;
            }
            System.out.println(j * 100_000 + ": " + winnings/(100_000 * j));
        }
    }

    public static boolean playerStrategySaysToHit(Hand playerHand, Hand dealerHand) {
        if (dealerHand.bestScore == 11 && playerHand.bestScore < 11) {
            return true;
        }
        if (playerHand.dominoes.size() == 3 && playerHand.uniquePipsCount == 2) {
            return playerHand.bestScore != 10;
        }
        int adjScore = playerHand.bestScore - playerHand.dominoes.size() + playerHand.uniquePipsCount;
        return adjScore <= 10;
    }

    public static void enumerateHands() {
        Tree tree = new Tree(6);
        int[] twoDominoScores = new int[19];
        int twoDominoHandCounts = 0;
        int[] threeDominoScores = new int[22];
        int threeDominoHandCounts = 0;
        int[] fourDominoScores = new int[22];
        int fourDominoHandCounts = 0;
        // Points before and after hitting on two domino hand.
        int[][] combinedThreeDominoScores = new int[19][22];
        // Points before and after hitting on three domino hand.
        int[][] combinedFourDominoScores = new int[22][22];
        // Points before and after hitting on two domino hand, by unique pips counts.
        int[][][] combinedThreeDominoScoresWithPips = new int[5][19][22];
        // Points before and after hitting on three domino hand, by unique pips counts.
        int[][][] combinedFourDominoScoresWithPips = new int [7][22][22];

        for (Node twoDominoNode : tree.nodes) {
            twoDominoScores[twoDominoNode.hand.bestScore] += 1;
            twoDominoHandCounts += 1;
            if (twoDominoNode.hand.bestScore < 12) twoDominoNode.addAllChildren();
            for (Node threeDominoNode : twoDominoNode.children) {
                if (twoDominoNode.hand.bestScore > threeDominoNode.hand.bestScore) {
                    System.out.println("Is this possible?");
                }
                threeDominoScores[threeDominoNode.hand.bestScore] += 1;
                threeDominoHandCounts += 1;
                combinedThreeDominoScores[twoDominoNode.hand.bestScore][threeDominoNode.hand.bestScore] += 1;
                combinedThreeDominoScoresWithPips[twoDominoNode.hand.uniquePipsCount][twoDominoNode.hand.bestScore][threeDominoNode.hand.bestScore] += 1;
                if (threeDominoNode.hand.bestScore < 12) threeDominoNode.addAllChildren();
                for (Node fourDominoNode : threeDominoNode.children) {
                    fourDominoScores[fourDominoNode.hand.bestScore] += 1;
                    fourDominoHandCounts += 1;
                    combinedFourDominoScores[threeDominoNode.hand.bestScore][fourDominoNode.hand.bestScore] += 1;
                    combinedFourDominoScoresWithPips[threeDominoNode.hand.uniquePipsCount][threeDominoNode.hand.bestScore][fourDominoNode.hand.bestScore] += 1;
                }
            }
        }
        System.out.println("Done.");
    }

    public static ArrayList<Domino> createShoe(int maxPipsPerEnd, int numOfDecks) {
        ArrayList<Domino> shoe = new ArrayList<>();
        for (int i = 0; i < numOfDecks; i++) {
            for (int j = 0; j <= maxPipsPerEnd; j++) {
                for (int k = j; k <= maxPipsPerEnd; k++) {
                    shoe.add(new Domino(j, k));
                }
            }
        }
        Collections.shuffle(shoe);
        return shoe;
    }

    public static void dealDominoesToHand(Hand hand, ArrayList<Domino> shoe, int numberOfDominoes) {
        for (int i = 0; i < numberOfDominoes; i++) {
            hand.addDomino(shoe.get(0));
            shoe.remove(0);
        }
    }
}
