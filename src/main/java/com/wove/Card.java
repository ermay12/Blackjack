package com.wove;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a standard playing card.
 */
class Card {
    /** The width of the card's representation in the terminal */
    public static final int ASCII_REPRESENTATION_WIDTH = 4;
    /** The height of the card's representation in the terminal */
    public static final int ASCII_REPRESENTATION_HEIGHT = 4;
    /** suit of the card */
    private final Suit suit;
    /** rank of the card */
    private final Rank rank;
    /** whether the card is face up or not */
    private boolean visible;

    /**
     * creates a new card with the given rank and suit
     * @param rank rank of new card
     * @param suit suit of new card
     */
    public Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
        visible = true;
    }

    /**
     * determines if the card is face up or face down. Face up
     * is visible.  All cards will be face up in this game except
     * for the dealer's first card.
     * @return if the card is face up
     */
    public boolean isVisible(){
        return visible;
    }

    /**
     * sets whether the card is face up (visible) or face down
     * (not visible)
     * @param visible if the card should be visible
     */
    public void setVisibility(boolean visible){
        this.visible = visible;
    }


    /**
     * gets the smallest value this card can represent. for aces it returns 1
     * @return the smallest value this card can represent. for aces it returns 1
     */
    public int getMinValue() {
        return rank.getValue();
    }

    /**
     * determines if this card is an ace
     * @return if this card is an ace
     */
    public boolean isAce(){
        return rank == Rank.ACE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Card)){
            return false;
        }
        Card other = (Card) obj;
        return this.suit == other.suit && this.rank == other.rank;
    }

    @Override
    public int hashCode() {
        return suit.hashCode()*31 + rank.hashCode();
    }

    @Override
    public String toString() {
        return "Card: " + rank + " of " + suit;
    }

    /**
     * gets a character array of how this card should appear in the terminal
     * @return character array of how this card should appear in the terminal
     */
    public char[][] getAsciiRepresentation() {
        char representation[][] = new char[ASCII_REPRESENTATION_HEIGHT][ASCII_REPRESENTATION_WIDTH];
        representation[0][0] = ' ';//'\u231C';
        representation[0][3] = ' ';//'\u231D';
        representation[3][0] = ' ';//'\u231E';
        representation[3][3] = ' ';//'\u231F';

        representation[0][1] = '-';
        representation[0][2] = '-';
        representation[3][1] = '-';
        representation[3][2] = '-';

        representation[1][0] = '|';
        representation[2][0] = '|';
        representation[1][3] = '|';
        representation[2][3] = '|';

        if(isVisible()) {
            representation[1][1] = rank.getChar();
            representation[2][2] = rank.getChar();
            representation[1][2] = suit.getChar();
            representation[2][1] = suit.getChar();
        }else {
            representation[1][1] = ' ';
            representation[2][2] = ' ';
            representation[1][2] = ' ';
            representation[2][1] = ' ';
        }
        return representation;
    }

    /**
     * getter for card's rank
     * @return card's rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * enum representing a card's suit
     */
    public enum Suit{
        DIAMONDS, HEARTS, SPADES, CLUBS;

        /**
         * gets a char representation of the suit
         * @return a char representation of the suit
         */
        public char getChar() {
            switch (this){
                case DIAMONDS:
                    return '\u2662';
                case HEARTS:
                    return '\u2661';
                case SPADES:
                    return '\u2660';
                case CLUBS:
                    return '\u2663';
            }
            return '?';
        }
    }

    /**
     * enum representing a card's rank
     */
    public enum Rank{
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;

        /**
         * Gets the nominal value of each rank.  Aces are counted as 1 here but
         * special cases must be made for hand evaluation of aces.
         * @return nominal value of rank
         */
        public int getValue() {
            switch (this) {
                case ACE:
                    return 1;
                case TWO:
                    return 2;
                case THREE:
                    return 3;
                case FOUR:
                    return 4;
                case FIVE:
                    return 5;
                case SIX:
                    return 6;
                case SEVEN:
                    return 7;
                case EIGHT:
                    return 8;
                case NINE:
                    return 9;
                default:
                    return 10;
            }
        }

        /**
         * gets a char representation of the rank
         * @return a char representation of the rank
         */
        public char getChar() {
            switch (this) {
                case ACE:
                    return 'A';
                case TWO:
                    return '2';
                case THREE:
                    return '3';
                case FOUR:
                    return '4';
                case FIVE:
                    return '5';
                case SIX:
                    return '6';
                case SEVEN:
                    return '7';
                case EIGHT:
                    return '8';
                case NINE:
                    return '9';
                case TEN:
                    return 'T';
                case JACK:
                    return 'J';
                case QUEEN:
                    return 'Q';
                case KING:
                    return 'K';
                default:
                    return ' ';
            }
        }
    }
}
