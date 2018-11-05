package com.wove;

import java.util.*;

/**
 * This class starts and runs the game of blackjack.
 * To use it, instantiate GameController and call startGame()
 */
public class GameController {
    /** List of players playing the game. */
    private final List<Player> players;
    /** representation of the dealer at a blackjack table */
    private final Dealer dealer;

    /** gameDone determines if the user would no longer like to use this application */
    private boolean gameDone;

    /**
     * Creates an instance of GameController that can be run
     */
    public GameController(){
        this.gameDone = false;
        this.players = new LinkedList<>();
        //pass a reference of the players to the dealer
        this.dealer = new Dealer(players);
    }

    /**
     * Starts the whole game
     */
    public void startGame(){
        //welcome users and get their names
        initGame();
        //loop for each round of Blackjack
        while(!gameDone) {
            //take bets and hand out cards
            startRound();
            //blackjack means the dealer drew an Ace and ten card first time
            if(dealer.hasBlackjack()) {
                //tell the users that the round is over and ask if they are done playing
                gameDone = handleDealerHasBlackjack();
                continue;
            }
            //Pay the players who got blackjack (21 on the first two cards)
            payPlayersWithBlackjack();
            //go through each player and ask them what to do
            for(Player player : players){
                //loop through each player's hand.  using an index to allow for adding hands on the fly
                for(int i = 0; i < player.getHands().size(); i++){
                    PlayerAction action;
                    Hand hand = player.getHands().get(i);
                    //skip if the hand is a natural blackjack. those people are done and paid
                    if(hand.isNaturalBlackJack()){
                        continue;
                    }
                    //loop until no more actions are to be done on a given hand
                    do {
                        Set<PlayerAction> availableActions = getAvailableActions(hand, player);
                        action = UserIO.getPlayerAction(player, hand, players, dealer, availableActions);
                        performPlayerAction(action, player, hand);
                        UserIO.clearScreen();
                        UserIO.printAllHands(hand, players, dealer);
                    }while(action != PlayerAction.STAND && action != PlayerAction.DOUBLE_DOWN  && !hand.isBust());
                }
            }
            UserIO.timeForDealerToDraw(players, dealer);
            //Show the dealer drawing cards
            dealerDraws();
            //dealer evaluates all player's hands and scores them
            dealer.collectBets();
            //show the results and ask if the user wants to play again
            gameDone = UserIO.isPlayerDone(players, dealer);
        }

        UserIO.thanksForPlaying(players);
    }


    /**
     * Initializes a game of blackjack
     * welcomes and determines the number of people playing and
     * shuffles the deck
     */
    private void initGame(){
        UserIO.printWelcomeMessage();
        List<String> names = UserIO.getNames();
        for(String name : names){
            players.add(new Player(name));
        }
    }

    /**
     * starts a round of blackjack.
     * Gets the bets of each player
     */
    private void startRound(){
        UserIO.clearScreen();
        UserIO.beginRound();
        Map<Player, Integer> bets = UserIO.getBets(players);
        for(Player player : players){
            //clears the player's old hand and gives them a new hand
            player.newHand(bets.get(player));
        }
        handOutCards();
    }

    /**
     * have the dealer hand out the cards one by one and updates the screen
     * for every new card.
     */
    private void handOutCards() {
        dealer.startRound();
        while(!dealer.doneHandingOutCards()){
            dealer.handOutCard();
            UserIO.clearScreen();
            UserIO.printAllHands(null, players, dealer);
            System.out.println();
            UserIO.pause(800);
        }
    }

    /**
     * pay players who have a blackjack.  Blackjack
     * payout is found in CasinoRules.  Explain what
     * happened afterwards
     */
    private void payPlayersWithBlackjack() {
        dealer.payPlayersWithBlackJack();
    }

    /**
     * collects money of players without blackjack and returns money to
     * players with blackjack.  Then explain what happened and
     * print out updated moneys.
     * @return
     */
    private boolean handleDealerHasBlackjack(){
        for(Player player: players){
            if(player.getHands().get(0).getValue() == 21){
                player.getHands().get(0).setNaturalBlackJack();
            }
        }
        dealer.collectBets();
        dealer.flipCards();
        UserIO.clearScreen();
        UserIO.printAllHands(null, players, dealer);
        return UserIO.dealerHasBlackjack();
    }

    /**
     * Performs the action the player requested or asks the dealer to do so.
     */
    private void performPlayerAction(PlayerAction action, Player player, Hand hand){
        switch(action){
            case HIT:
                dealer.hit(hand);
                break;
            case DOUBLE_DOWN:
                player.doubleDown(hand);
                dealer.hit(hand);
                break;
            case SPLIT:
                player.split(hand);
                break;
        }
    }

    /**
     * gets the available actions to a player on a given turn.
     * @param hand current hand of interest
     * @param player current player
     * @return the available actions
     */
    private Set<PlayerAction> getAvailableActions(Hand hand, Player player){
        Set<PlayerAction> availableActions = new HashSet<>();
        availableActions.add(PlayerAction.STAND);
        if(hand.canHit()){
            availableActions.add(PlayerAction.HIT);
        }
        if(player.getNumberOfSplits() < CasinoRules.MAX_NUMBER_OF_SPLITS && hand.canSplit()){
            availableActions.add(PlayerAction.SPLIT);
        }
        if(CasinoRules.mayDoubleDown(hand)){
            availableActions.add(PlayerAction.DOUBLE_DOWN);
        }
        return availableActions;
    }

    /**
     * Has the dealer draw his cards one by one updating the screen each time
     */
    private void dealerDraws(){
        dealer.flipCards();
        UserIO.clearScreen();
        UserIO.printAllHands(dealer.getHand(), players, dealer);
        System.out.println();
        while(dealer.mustHit()){
            UserIO.pause(800);
            dealer.hitDealer();
            UserIO.clearScreen();
            UserIO.printAllHands(dealer.getHand(), players, dealer);
            System.out.println();
        }
        UserIO.pause(800);
    }
}
