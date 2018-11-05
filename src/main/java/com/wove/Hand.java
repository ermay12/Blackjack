package com.wove;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * class to represent the cards in a hand for a player on a given round
 */
class Hand {
    /** Height of a hand of cards in the terminal when print */
    public static final int ASCII_REPRESENTATION_HEIGHT = Card.ASCII_REPRESENTATION_HEIGHT+2;
    /** the bet in dollars placed on this hand */
    private int bet;
    /** the cards in this hand */
    private List<Card> cards;
    /** whether this hand has been split already. Affects when this hand can be hit*/
    private boolean splitHand;
    /** whether this hand has a natural black jack (21 when first dealt)*/
    private boolean naturalBlackJack;

    /**
     * Creates an empty hand with no cards and a specified bet
     * @param bet money bet on this hand
     */
    public Hand(int bet){
        this.bet = bet;
        cards = new LinkedList<>();
        splitHand = false;
        naturalBlackJack = false;
    }

    /**
     * gets the number of cards in this hand
     * @return number of cards in this hand
     */
    public int numberOfCards(){
        return cards.size();
    }

    /**
     * getter for the bet placed on this hand
     * @return bet placed on this hand
     */
    public int getBet() {
        return bet;
    }

    /**
     * add more money to bet
     * @param addition money to add to bet
     */
    public void addToBet(int addition) {
        bet += addition;
    }

    /**
     * Splits this hand and adds a card to the new hand
     * @param newHand new hand created by the split
     */
    public void split(Hand newHand) {
        newHand.cards.add(cards.remove(1));
        splitHand = true;
        newHand.splitHand = true;
    }

    /**
     * determines if this hand can be hit.  When a hand of Aces
     * is split, it can only be hit once.
     * @return if this hand can be hit
     */
    public boolean canHit(){
        return !(splitHand && cards.get(0).isAce() && cards.size() == 2);
    }

    /**
     * checks if this is a hand that can be split
     * @return whether this hand can be split
     */
    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).getRank().equals(cards.get(1).getRank());
    }

    /**
     * determines if this hand is a bust
     * @return if this hand is a bust
     */
    public boolean isBust() {
        int total = 0;
        for(Card card : cards){
            total += card.getMinValue();
        }
        return total > 21;
    }

    /**
     * determines the largest value this hand can represent without going over 21
     * @return the largest value this hand can represent without going over 21
     */
    public int getValue(){
        /*
        The general algorithm here is to add all the cards with a definite value
        including the aces as 1 because these are present in the sum always.
        Then for each ace, see if we can switch its value to 11 without going
        over.  If we cant, then we simply switch it back to 1 and return the value.
         */
        int value = 0;
        int numberOfAces = 0;
        for(Card card : cards){
            value += card.getMinValue();
            if(card.isAce()){
                numberOfAces++;
            }
        }
        for(int i = 0; i < numberOfAces; i++){
            value+=10;
            if(value > 21){
                return value - 10;
            }
        }
        return value;
    }

    /**
     * Add a new card to this hand
     * @param card
     */
    public void addCard(Card card) {
        cards.add(card);
        if(isBust()){
            loseBet();
        }
    }

    /**
     * Player either bust or lost to the dealer and loses their bet
     */
    public void loseBet() {
        bet = 0;
    }

    /**
     * getter for whether this is a natural blackjack
     * @return whether this is a natural blackjack
     */
    public boolean isNaturalBlackJack() {
        return naturalBlackJack;
    }

    /**
     * sets this hand as a natural BlackJack
     */
    public void setNaturalBlackJack() {
        this.naturalBlackJack = true;
    }

    /**
     * returns a character reprentation of the player's hand.
     * @param currentHand if this hand is the hand being referenced by whatever
     *                    called it
     * @return the representation in a character array
     */
    public char[][] getAsciiRepresentation(boolean currentHand, boolean displayBet) {
        /*
        This function loads the ascii representations of each card into a new character array
        and adds details about the bet, the hand being bust, blackjack, or the current highlighted hand.
         */
        int numberOfCards = cards.size();
        int representationWidth = numberOfCards*Card.ASCII_REPRESENTATION_WIDTH + 1;
        int minWidth = Math.max(String.valueOf(bet).length()+1, Card.ASCII_REPRESENTATION_WIDTH*2+1);
        representationWidth = Math.max(representationWidth, minWidth);
        char representation[][] = new char[ASCII_REPRESENTATION_HEIGHT][representationWidth];
        for(int r  = 0; r < representation.length; r++){
            Arrays.fill(representation[r], ' ');
        }
        int column = 0;
        for(Card card : cards){
            char cardRepresentation[][] = card.getAsciiRepresentation();
            for(int r = 0; r < cardRepresentation.length; r++){
                for(int c = 0; c < cardRepresentation[0].length; c++){
                    representation[r][column+c] = cardRepresentation[r][c];
                }
            }
            column += cardRepresentation[0].length;
        }
        assert(!(currentHand && isNaturalBlackJack()));
        if(currentHand){
            int row = Card.ASCII_REPRESENTATION_HEIGHT;
            representation[row][(representation[0].length-1)/2] = '^';
        } else if(isNaturalBlackJack()){
            int row = Card.ASCII_REPRESENTATION_HEIGHT;
            String blackjackString = "*BLKJCK*";
            for(int c = 0; c < blackjackString.length(); c++){
                representation[row][c] = blackjackString.charAt(c);
            }
        }else if(isBust()) {
            int row = Card.ASCII_REPRESENTATION_HEIGHT;
            String bustString = "BUST";
            int bustStart = (representation[0].length - bustString.length()) / 2;
            for (int c = 0; c < representation[0].length - 1; c++) {
                if (c >= bustStart && c < bustStart + bustString.length()) {
                    representation[row][c] = bustString.charAt(c - bustStart);
                } else {
                    representation[row][c] = '#';
                }
            }
        }
        if(!displayBet){
            return representation;
        }
        int row = representation.length-1;
        String betString = "$" + String.valueOf(bet);
        int betStart = (representation[0].length - betString.length())/2;
        for(int c = betStart; c < betStart + betString.length(); c++){
            representation[row][c] = betString.charAt(c - betStart);
        }
        return representation;
    }

    /**
     * flip upwards all cards in this hand
     */
    public void flipCards() {
        for(Card card : cards){
            card.setVisibility(true);
        }
    }
}
