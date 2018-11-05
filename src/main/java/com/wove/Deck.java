package com.wove;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * class representing the deck of all cards used by the dealer.  May contain
 * more than one standard playing card deck (52 cards).  See CasinoRules for
 * the number of decks used.
 *
 * Also an important thing to note is that this deck auto resets and shuffles
 * when there are only 25% of the original cards left.
 */
class Deck {
    /** Number of cards in a standard playing deck */
    public static final int CARDS_PER_SINGLE_DECK = 52;
    /** number of cards in our combined deck */
    public static final int CARDS_PER_SUPER_DECK = CARDS_PER_SINGLE_DECK * CasinoRules.NUMBER_OF_DECKS;
    /** list holding all cards in deck. The first card in the list is the top of the deck */
    private final List<Card> cards;

    /**
     * create a new deck object with no cards in it.
     */
    public Deck(){
        cards = new LinkedList<>();
        resetDeck();
        shuffleDeck();
    }

    /**
     * add all cards to the deck
     */
    private void resetDeck(){
        cards.clear();
        for(int i = 0; i < CasinoRules.NUMBER_OF_DECKS; i++){
            for(Card.Rank rank : Card.Rank.values()){
                for(Card.Suit suit : Card.Suit.values()){
                    cards.add(new Card(rank, suit));
                }
            }
        }
    }

    /**
     * shuffle all the cards in this deck
     */
    private void shuffleDeck(){
        Collections.shuffle(cards);

    }

    /**
     * gets the card on top of the deck.  Removes it permanently
     * @return the top card of the deck.
     */
    public Card getCard(){
        if(cards.size() < CARDS_PER_SUPER_DECK  / 4){
            resetDeck();
            shuffleDeck();
        }
        return cards.remove(0);
    }

}
