package dominoes;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        int MAX_PIPS_PER_END = 6;
        int NUM_DECKS = 1;
        int DEALER_STAY = 10;
        int SIM_PLAYER_STAY = 10;
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
                // Player hits until stand constant (this is a naïve strategy - it is likely that a better one exists).
                while(playerHand.bestScore < SIM_PLAYER_STAY && playerHand.dominoes.size() < MAX_HAND_SIZE) {
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

    public static ArrayList<Domino> createShoe(int maxPipsPerEnd, int numOfDecks) {
        ArrayList<Domino> shoe = new ArrayList<>(maxPipsPerEnd / 2 * (maxPipsPerEnd + 1) * numOfDecks);
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
