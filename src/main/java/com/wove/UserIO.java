package com.wove;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * class that handles all communication with the user
 */
class UserIO {
    /** Name lengths are limited because of limited screen space */
    private static final int MAX_NAME_LENGTH = 10;
    /** The largest heigh the console can be to clear the screen in units of lines */
    private static final int MAX_CONSOLE_HEIGHT = 50;
    /** the smallest the console can be in terms of width */
    private static final int MIN_CONSOLE_WIDTH = 79;

    /** Scanner object for user input */
    private static final Scanner scanner = new Scanner(System.in);


    /**
     * prints a welcome message to the user explaining the house rules
     */
    public static void printWelcomeMessage(){
        clearScreen();
        System.out.println("Hi, Welcome to Wove Casino.");
        System.out.println("Let's play Blackjack!");
        System.out.println();
        System.out.println("These are the table rules:");
        System.out.println("     " + CasinoRules.MIN_MAX_BET_STRING);
        System.out.println("     " + CasinoRules.BLACKJACK_PAYOUT_STRING);
        System.out.println("     " + CasinoRules.DEALER_STAND_RULE_STRING);
        System.out.println("     " + CasinoRules.DOUBLE_DOWN_RULE);
        System.out.println("     " + CasinoRules.SPLIT_RULE_STRING);
        System.out.println("     " + CasinoRules.NUMBER_OF_DECKS_STRING);
        System.out.println();
        System.out.print("<Press enter to continue>");
        waitForEnter();
    }

    /**
     * Gets the names of all players
     * @return List of the player's names
     */
    public static List<String> getNames() {
        clearScreen();
        System.out.println("Only 1 to 7 players may play at this table.");
        System.out.print("How many players would like to play?: ");
        Integer numberOfPlayers = getInteger(CasinoRules.MIN_NUMBER_PLAYERS , CasinoRules.MAX_NUMBER_PLAYERS);
        List<String> names = new LinkedList<>();
        clearScreen();
        System.out.println("Great! " + numberOfPlayers + " players will be at this table.");
        System.out.println();
        if(scanner.hasNextLine()) {
            scanner.nextLine();
        }
        for(int i = 1; i <= numberOfPlayers; i++){
            System.out.print("Player " + i + ", please enter your name: ");
            String name = scanner.nextLine();
            if(name.length() > MAX_NAME_LENGTH){
                name = name.substring(0,MAX_NAME_LENGTH);
            }
            names.add(name);
            clearScreen();
        }
        return names;

    }

    /**
     * helper function for getting integer input from the user
     * @param min min value that can be received
     * @param max max value that can be received
     * @return the input integer
     */
    private static int getInteger(int min, int max){
        int input = 0;
        do{
            while(!scanner.hasNextInt()){
                scanner.next();
                System.out.print("Not a number.  Please Enter a number between "+min+" and "+max+":");
            }
            input = scanner.nextInt();
            if(input < min || input > max){
                System.out.print("Invalid number. Please enter a number between "+min+" and "+max+":");
            }
        }while(input < min || input > max);

        return input;
    }

    /**
     * pauses the game for "milliSeconds" milliseconds
     * @param milliSeconds number of milliseconds to pause for
     */
    static void pause(int milliSeconds){
        try{
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        } catch (InterruptedException e){
        }
    }

    /**
     * Pauses the game until the user presses enter.
     */
    static void waitForEnter(){
        scanner.nextLine();
    }

    /**
     * Asks the user if they would like to play another round
     * @return true if the player is done
     */
    public static boolean isPlayerDone(List<Player> players, Dealer dealer) {
        clearScreen();
        printAllHands(null, players, dealer);
        System.out.println("The dealer is done drawing.  Here are the results.");
        System.out.print("Would you like to play another round?(y/n): ");
        while(true){
            String input = scanner.nextLine();
            if(input.toLowerCase().equals("y")){
                return false;
            }else if(input.toLowerCase().equals("n")){
                return true;
            }
        }
    }

    /**
     * Asks what the player would like to do next
     * @param player current player being polled
     * @param hand current hand being polled
     * @param players all players
     * @param dealer the dealer
     * @param availableActions available actions to the user
     * @return the chosen action
     */
    public static PlayerAction getPlayerAction(Player player, Hand hand, List<Player> players, Dealer dealer, Set<PlayerAction> availableActions) {
        clearScreen();
        printAllHands(hand, players, dealer);
        System.out.println(player.getName() + ", you may do the following. ");
        System.out.print("Choose one of these(");
        for(PlayerAction action : availableActions){
            System.out.print(" <" + action + ">");
        }
        System.out.print("): ");
        PlayerAction chosenAction = null;
        while (chosenAction == null){
            try{
                chosenAction = PlayerAction.valueOf(scanner.nextLine().toUpperCase());
            }catch(Exception e){
                System.out.println("Not a valid action.  Please choose one from the list given.");
                System.out.print("Choose one of these(");
                for(PlayerAction action : availableActions){
                    System.out.print(" <" + action + ">");
                }
            }
        }
        return chosenAction;
    }

    /**
     * prints the dealer's hand and all of the player's hands
     * @param currentHand the hand to be highlighted. null means nothing is highlighted
     * @param players the players of the game.
     * @param dealer the dealer
     */
    public static void printAllHands(Hand currentHand, List<Player> players, Dealer dealer) {

        char lineBuffer[][] = new char[HandHolder.ASCII_REPRESENTATION_HEIGHT][MIN_CONSOLE_WIDTH];

        char dealerBuffer[][] = dealer.getAsciiRepresentation(currentHand);
        for(int r = 0; r < dealerBuffer.length; r++){
            for(int c = 0; c < dealerBuffer[r].length; c++){
                lineBuffer[r][c] = dealerBuffer[r][c];
            }
        }
        printBuffer(lineBuffer);
        lineBuffer = new char[HandHolder.ASCII_REPRESENTATION_HEIGHT][MIN_CONSOLE_WIDTH];
        int column = 0;
        for(Player player : players){
            char playerBuffer[][] = player.getAsciiRepresentation(currentHand);
            if(column + playerBuffer[0].length > MIN_CONSOLE_WIDTH){
                printBuffer(lineBuffer);
                lineBuffer = new char[Player.ASCII_REPRESENTATION_HEIGHT][MIN_CONSOLE_WIDTH];
                System.out.println();
                column = 0;
            }
            for(int r = 0; r < playerBuffer.length; r++){
                for(int c = 0; c < playerBuffer[r].length; c++){
                    lineBuffer[r][column+c] = playerBuffer[r][c];
                }
            }
            column += playerBuffer[0].length;

        }
        printBuffer(lineBuffer);
    }

    /**
     * printes the buffer to the screen
     * @param buffer buffer to be print
     */
    private static void printBuffer(char buffer[][]){
        for(int r = 0; r < buffer.length; r++){
            System.out.println(buffer[r]);
        }
    }

    /**
     * tells the players that the round is done and the dealer must draw his cards
     * @param players players of the game
     * @param dealer the dealer
     */
    public static void timeForDealerToDraw(List<Player> players, Dealer dealer) {
        clearScreen();
        printAllHands(null, players, dealer);
        System.out.println("All player actions are done.  Time for the dealer to draw.");
        System.out.print("<Enter to continue>");
        waitForEnter();
    }

    /**
     * tells the players that the round is starting
     */
    public static void beginRound() {
        System.out.println("Ok, let's start this round.");
        System.out.println();
    }

    /**
     * gets the bets of each player and stores them in a map
     * that maps each player to their bet
     * @param players players in the game
     * @return a map holding each player's bet
     */
    public static Map<Player, Integer> getBets(List<Player> players) {
        Map<Player, Integer> bets = new HashMap<>();
        System.out.println("This is how much money everyone has.");
        System.out.println();
        for(Player player : players){
            printMoney(players, player);
            System.out.println();
            System.out.print(player.getName() + ", please enter your bet:");
            int bet = getInteger(CasinoRules.MIN_BET, Math.min(player.getMoney(),CasinoRules.MAX_BET));
            bets.put(player, bet);
            clearScreen();
        }
        System.out.println("Great, all bets have been collected.");
        System.out.println();
        System.out.print("<Press enter to begin handing out cards>");
        waitForEnter();
        return bets;
    }

    /**
     * prints the money of each player and highlights the current player
     * @param players all players
     * @param currrentPlayer player to highlight.  null if none
     */
    private static void printMoney(List<Player> players, Player currrentPlayer){
        System.out.print("|");
        for(Player player : players){
            int nameLength = player.getName().length();
            int nameStart = (MAX_NAME_LENGTH - nameLength)/2;
            for(int i = 0; i < nameStart; i++){
                System.out.print(" ");
            }
            System.out.print(player.getName());
            for(int i = 0; i < MAX_NAME_LENGTH - (nameLength + nameStart); i++){
                System.out.print(" ");
            }
            System.out.print("|");
        }
        System.out.println();
        System.out.print("|");
        for(Player player : players){
            int moneyLength = String.valueOf(player.getMoney()).length() + 1;
            int moneyStart = (MAX_NAME_LENGTH - moneyLength)/2;
            for(int i = 0; i < moneyStart; i++){
                System.out.print(" ");
            }
            System.out.print("$" + player.getMoney());
            for(int i = 0; i < MAX_NAME_LENGTH - (moneyLength + moneyStart); i++){
                System.out.print(" ");
            }
            System.out.print("|");
        }
        System.out.println();
        if(currrentPlayer == null){
            System.out.println();
        }else{
            int arrowStart = players.indexOf(currrentPlayer) * (MAX_NAME_LENGTH+1) + MAX_NAME_LENGTH/2;
            for(int i = 0; i < arrowStart; i++){
                System.out.print(" ");
            }
            System.out.println("^");
        }
    }

    /**
     * clears the screen with whitespace
     */
    public static void clearScreen(){
        StringBuilder newLines = new StringBuilder();
        for(int i = 0; i < MAX_CONSOLE_HEIGHT; i++){
            newLines.append('\n');
        }
        System.out.println(newLines.toString());
    }

    /**
     * tells the users that the dealer has black jack and asks
     * if the users would like to play again
     * @return users are done playing
     */
    public static boolean dealerHasBlackjack() {
        System.out.println("The dealer has Blackjack.  Thus the round is over and money has been collected.");
        System.out.print("Would you like to play another round?(y/n): ");
        while(true){
            String input = scanner.nextLine();
            if(input.toLowerCase().equals("y")){
                return false;
            }else if(input.toLowerCase().equals("n")){
                return true;
            }
        }
    }

    /**
     * prints the exit message
     * @param players all players
     */
    public static void thanksForPlaying(List<Player> players) {UserIO.clearScreen();
        printMoney(players, null);
        System.out.println();
        System.out.println("Thank you for playing today.  Here are the results.");
        System.out.print("<Press Enter to Exit>");
        waitForEnter();
    }
}
