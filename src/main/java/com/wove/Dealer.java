package com.wove;

import java.util.LinkedList;
import java.util.List;

/**
 * class to represent the dealer in Blackjack
 */
class Dealer extends HandHolder{
    /** reference to the players at the table */
    private final List<Player> players;
    /** the dealer's hand */
    private Hand hand;
    /** the deck of cards.  Composed of more than one standard deck*/
    private final Deck deck;
    /** a reference to the person the dealer will give the next card to
     * used when dealing cards only.  Not when hitting */
    private HandHolder currentHandHolderBeingDealt;

    /**
     * creates a new instance of a dealer who has a reference to each player
     * @param players players the dealer deals to
     */
    public Dealer(List<Player> players){
        this.players = players;
        deck = new Deck();
    }

    /**
     * determines if the dealer has a natural blackjack
     * @return if the dealer has a natural blackjack
     */
    public boolean hasBlackjack() {
        if(hand.getValue() == 21 && hand.numberOfCards() == 2){
            hand.setNaturalBlackJack();
            return true;
        }
        return false;
    }

    /**
     * put a card in the dealer's hand
     */
    public void hitDealer() {
        hand.addCard(deck.getCard());
    }

    /**
     * Determines if the dealer should hit themself
     * @return true if the dealer should hit themself
     */
    public boolean mustHit() {
        if(hand.isBust()){
            return false;
        }
        //See if the dealer has already one.
        //I'm a little fuzzy on the rules her
        //but it seems like common sense for the
        //dealer to stop if that's the case.
        boolean dealerBeatsAll = true;
        for(Player player : players){
            for(Hand playerHand : player.getHands()) {
                if (!playerHand.isBust() && playerHand.getValue() > hand.getValue()) {
                    dealerBeatsAll = false;
                    break;
                }
            }
        }
        if(dealerBeatsAll){
            return false;
        }
        //otherwise reference the official hit policy in the rules class
        return CasinoRules.dealerHitPolicy(hand);
    }

    /**
     * getter for the dealer's hand
     * @return hand of the dealer
     */
    public Hand getHand() {
        return hand;
    }

    @Override
    public List<Hand> getHands() {
        List<Hand> hands = new LinkedList<>();
        hands.add(getHand());
        return hands;
    }

    @Override
    String getName() {
        return "Dealer";
    }

    @Override
    boolean shouldDisplayBet() {
        return false;
    }


    /**
     * collect the bets from each losing player
     * and payout the winners and ties
     */
    public void collectBets() {
        int dealerHandValue = hand.getValue();
        boolean dealerBust = hand.isBust();

        //Iterate through every player's hands
        for (Player player : players){
            for (Hand playerHand : player.getHands()){
                if(hasBlackjack()) {
                    if (playerHand.getValue() < dealerHandValue) {
                        playerHand.loseBet();
                    }
                }else if(playerHand.isNaturalBlackJack()){
                    ;
                }else if(dealerBust) {
                    if (!playerHand.isBust()) {
                        playerHand.addToBet(playerHand.getBet());
                    }
                }else{
                    if (dealerHandValue > playerHand.getValue()) {
                        playerHand.loseBet();
                    } else if(dealerHandValue < playerHand.getValue()){
                        playerHand.addToBet(playerHand.getBet());
                    }
                }
                player.addMoney(playerHand.getBet());
            }
        }
    }

    /**
     * initialize the dealer for the new round
     */
    public void startRound() {
        hand = new Hand(0);
        currentHandHolderBeingDealt = players.get(0);
    }

    /**
     * determine if every player has all of their cards
     * @return true if every player has all of their cards
     */
    public boolean doneHandingOutCards() {
        return currentHandHolderBeingDealt.getHands().get(0).numberOfCards() == 2;
    }

    /**
     * Hands out a single card in a circle to all players and the dealer themself
     */
    public void handOutCard() {
        /*
            general overview of how this works.  The dealer hands out cards one by
            one in a circle.  The currentHandHolderBeingDealt references who will get
            the next card.  After dealing the card, the currentHandHolderBeingDealt is
            updated.
         */
        Hand currentHand = currentHandHolderBeingDealt.getHands().get(0);
        if(currentHandHolderBeingDealt.equals(this) && hand.numberOfCards() == 0){
            Card card = deck.getCard();
            card.setVisibility(false);
            currentHand.addCard(card);
        }else {
            currentHand.addCard(deck.getCard());
        }
        //if dealer just dealt himself, deal player 1 again
        if(currentHandHolderBeingDealt.equals(this)){
            currentHandHolderBeingDealt = players.get(0);
            return;
        }
        // otherwise a player is being dealt and we can get the index of them
        int playerIndex = players.indexOf((Player)currentHandHolderBeingDealt);
        // if the current player is the last player, deal the dealer next
        if (playerIndex == players.size()-1){
            currentHandHolderBeingDealt = this;
        } else {//deal the next player
            currentHandHolderBeingDealt = players.get(playerIndex+1);
        }
    }

    /**
     * Pay each player who has a natural blackjack the appropriate amount
     */
    public void payPlayersWithBlackJack() {
        for(Player player : players){
            Hand playerHand = player.getHands().get(0);
            if(playerHand.getValue() == 21){
                playerHand.setNaturalBlackJack();
                playerHand.addToBet((int)(((double)playerHand.getBet())*CasinoRules.BLACKJACK_PAYOUT));
            }
        }
    }

    /**
     * gives the respective hand a new card
     * @param hand hand to be hit
     */
    public void hit(Hand hand) {
        hand.addCard(deck.getCard());
    }

    /**
     * flip over hidden cards
     */
    public void flipCards() {
        hand.flipCards();
    }
}
