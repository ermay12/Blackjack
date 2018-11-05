package com.wove;

import java.util.LinkedList;
import java.util.List;

/**
 * Anyone who holds hands of cards.  So either a player or the dealer.
 * Used when the dealer hands out cards and to draw every person's
 * cards to the screen.
 */
abstract class HandHolder {

    /** Height of the ascii representation */
    public static final int ASCII_REPRESENTATION_HEIGHT = Hand.ASCII_REPRESENTATION_HEIGHT+1;
    /**
     * getter for a HandHolder's hands
     * @return returns a list of all hands a hand holder is holding
     */
    abstract List<Hand> getHands();

    /**
     * getter for the person's name
     * @return person's name
     */
    abstract String getName();

    /**
     * Determines if the bet should be visible for this player's hands.
     * @return whether the bet should be visible
     */
    abstract boolean shouldDisplayBet();

    /**
     * turns all the player's hands into a character array that can be printed to the console.
     * @return a character array that can be printed to the console
     */
    public char[][] getAsciiRepresentation(Hand currentHand) {
        /*
        Loads the ascii representation of each hand and places the users
        name above them in a new character array
         */
        List<Hand> hands = getHands();
        List<char[][]> handRepresentations = new LinkedList<>();
        int width = 0;
        for(Hand hand : hands){
            char handRepresentation[][] = hand.getAsciiRepresentation(hand.equals(currentHand), shouldDisplayBet());
            width += handRepresentation[0].length;
            handRepresentations.add(handRepresentation);
        }
        String name = getName();
        char representation[][] = new char[ASCII_REPRESENTATION_HEIGHT][Math.max(width,name.length()+1)];
        int reprWidth = representation[0].length;
        int nameStart = (reprWidth-name.length()-1)/2;
        for(int i = 0; i < reprWidth-1; i++){
            if(i < nameStart || i >= nameStart+name.length()){
                representation[0][i] = '-';
            }else {
                representation[0][i] = name.charAt(i-nameStart);
            }
        }
        int column = 0;
        int row = 1;
        for(char handRepresentation[][] : handRepresentations) {
            for (int r = 0; r < handRepresentation.length; r++) {
                for (int c = 0; c < handRepresentation[0].length; c++) {
                    representation[row+r][column + c] = handRepresentation[r][c];
                }
            }
            column += handRepresentation[0].length;
        }
        for(int r = 1; r < representation.length; r++){
            for(int c = width; c < representation[0].length; c++){
                representation[r][c] = ' ';
            }
        }
        return representation;
    }

}
