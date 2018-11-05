package com.wove;

/**
 * Class describing and defining all Casino Rules
 * String constants are used for describing the rule to the user and
 * other constants are used in the games code.
 */
class CasinoRules {
    public static final String BLACKJACK_PAYOUT_STRING = "Blackjack pays 3 to 2.";
    public static final double BLACKJACK_PAYOUT = 1.5;

    public static final String DEALER_STAND_RULE_STRING = "Dealer must draw to and stand on all 17's.";

    /**
     * Determines when the dealer must hit
     * @param hand the dealer's hand
     * @return true if the dealer should hit and false if they should stand
     */
    public static boolean dealerHitPolicy(Hand hand){
        if(hand.getValue() < 17){
            return true;
        } else {
            return false;
        }
    }

    public static final String DOUBLE_DOWN_RULE = "May Double down on any pair of cards.";

    /**
     * Determines when a hand may double down.
     * @param hand current hand of the player
     * @return whether the player may double down on this hand
     */
    public static boolean mayDoubleDown(Hand hand){
        return hand.numberOfCards() == 2;
    }

    public static final int MIN_BET = 0;
    public static final int MAX_BET = Integer.MAX_VALUE;
    public static final String MIN_MAX_BET_STRING = "Min bet $" + MIN_BET + ". no max bet.";

    public static final int MAX_NUMBER_OF_SPLITS = 3;
    public static final String SPLIT_RULE_STRING = "May split up to " + MAX_NUMBER_OF_SPLITS + " times.";

    public static final int NUMBER_OF_DECKS = 4;
    public static final String NUMBER_OF_DECKS_STRING = "Number of decks in circulation: " + NUMBER_OF_DECKS;

    public static final int MIN_NUMBER_PLAYERS = 1;
    public static final int MAX_NUMBER_PLAYERS = 7;

    public static final int STARTING_MONEY = 1000;
}
