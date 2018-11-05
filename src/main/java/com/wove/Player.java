package com.wove;

import java.util.LinkedList;
import java.util.List;

/**
 * class to represent the players of Blackjack.
 */
class Player extends HandHolder{
    /** the player's money */
    private int money;
    /** the player's name */
    private final String name;
    /** the player's hands.  Can have more than one
     * if the player splits their first hand */
    private final List<Hand> hands;
    /** Number of times the player has split this round.
     * They may only split so many times */
    private int numberOfSplits;

    /**
     * creates a new player with the given name
     * @param name name of player
     */
    public Player(String name) {
        this.name = name;
        this.hands = new LinkedList<>();
        this.numberOfSplits = 0;
        this.money = CasinoRules.STARTING_MONEY;
    }

    /**
     * getter for player's hands.  A player can have more than
     * one hand if they split a hand.
     * @return the player's hands
     */
    public List<Hand> getHands(){
        return hands;
    }

    /**
     * clears all hands a player has from the last round
     * adds a new empty hand with the give bet
     * @param bet bet on the new hand for this round
     */
    public void newHand(int bet) {
        hands.clear();
        hands.add(new Hand(bet));
        money -= bet;
        numberOfSplits = 0;
    }

    /**
     * getter for the money a player currently owns
     * @return the money a player currently owns
     */
    public int getMoney() {
        return money;
    }

    /**
     * Doubles down on the given hand
     * @param hand
     */
    public void doubleDown(Hand hand) {
        hand.addToBet(Math.min(money, hand.getBet()));
        money -= Math.min(money, hand.getBet());
    }

    /**
     * getter for the number of times this player has split this round
     * @return number of times this player has split this round
     */
    public int getNumberOfSplits() {
        return numberOfSplits;
    }

    /**
     * splits this hand for the player.  Money on the split hand
     * comes from the casino and not the player's money.
     */
    public void split(Hand hand) {
        numberOfSplits++;
        Hand splitHand = new Hand(hand.getBet());
        hand.split(splitHand);
        hands.add(splitHand);
    }

    /**
     * add the winnings to the player's money
     * @param winnings the cash to be added to the player's money
     */
    public void addMoney(int winnings) {
        money += winnings;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    boolean shouldDisplayBet() {
        return true;
    }


}
