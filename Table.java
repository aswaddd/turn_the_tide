
/**
 * @author: TARIQ Aswad Mohammed (SID)22209786
 */
import java.util.Scanner;

public class Table {

    private static final int TOTAL_TURN = 12;

    private static final int TOTAL_TIDE_CARDS = TOTAL_TURN * 2;

    private static final int TOTAL_WEATHER_CARD = 60;

    private int[] tide = new int[TOTAL_TIDE_CARDS];

    private Weather[][] decks;

    private Player[] players;

    public static void main(String[] arg) {
        System.out.println("Welcome to the Turn the Tile game!");
        int player = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Please enter the number of players you want to play:");
            player = scanner.nextInt();
        } while (player < 2 || player > 5);
        new Table(player).run();
    }

    public Table(int player) {
        // prepare players array
        Player player1 = new Player("Anya Forger"); // first player is human
        Player[] tempPlayersArray = new Player[player];
        tempPlayersArray[0] = player1;

        for (int i = 1; i < player; i++) {
            Player aiPlayer = new Player(); // create an AI player and load it into the Players array
            tempPlayersArray[i] = aiPlayer;
        }
        players = tempPlayersArray;

        // prepare tide cards, 1 to 12, two sets
        int[] tempTide = { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12};
        tide = tempTide;

        int[][] MAGIC_NUMBERS = {
                { 31, 26, 54, 19, 8, 45, 39, 16, 28, 42, 3, 38 },  //deck1
                { 30, 60, 58, 56, 51, 10, 41, 48, 14, 9, 32, 44 },  //deck2
                { 29, 35, 7, 49, 1, 21, 13, 46, 6, 18, 43, 24 },   //deck3
                { 27, 34, 33, 11, 12, 47, 23, 55, 40, 37, 22, 15 }, //deck4, if any
                { 57, 36, 50, 20, 53, 52, 59, 4, 25, 2, 17, 5 }     //deck5, if any
        };

        // assign weather cards from the array weather to decks according to the
        // MAGIC_NUMBERS

        Weather[][] tempDecksArray = new Weather[players.length][MAGIC_NUMBERS[0].length];
        for (int i = 0; i < player; i++) {
            for (int j = 0; j < TOTAL_TURN; j++) {
                Weather card = new Weather(MAGIC_NUMBERS[i][j]); // create the weather card according to the number
                tempDecksArray[i][j] = card; // load it into the deck
            }
        }
        decks = tempDecksArray;
    }


    private void shuffleTideCard() {
        // A deterministic shuffle required by the program
        int[] order = { 4, 23, 8, 11, 3, 16, 21, 14, 0, 6, 18, 15, 19, 9, 5, 12, 13, 2, 17, 7, 10, 20, 1, 22 };
        int[] newTide = new int[TOTAL_TIDE_CARDS];
        for (int i = 0; i < TOTAL_TIDE_CARDS; i++) {
            newTide[i] = tide[order[i]];
        }
        tide = newTide;
    }

    public void run() {
        // The game should be running for N rounds.
        // We write the first round for you to get you started.
        for (int i = 0; i < players.length; i++) {
            int[] reSetTideDeck = { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12};
            tide = reSetTideDeck;
            for (int j = 0; j <= i; j++)
                shuffleTideCard();
            System.out.println();
            System.out.println();
            System.out.println("Round " + (i + 1) + " starts.");
            System.out.println("---------------------------------\n");
            oneRound(i);
        }
    }
    // calculate the number of players that have not been eliminated
    private int numberOfActivePLayers() {
        int count = players.length;
        for (int i = 0; i < players.length; i++) {
            if (players[i].isEliminated()) {
                count--;
            }
        }
        return count;
    }

    private void oneRound(int round) {
        // this method should do much more things and run at most 12 turns
        // unless there are less than 3 players remaining.
        // We write the first turn for you to get you started.
        int turns = 0;
        for (int i = 0; i < players.length; i++) { // dealing cards to the players
            for (int j = 0; j < decks[i].length; j++) {
                players[i].addCard(decks[i][j]);
            }
            players[i].calcLifePreservers(); // calculate before the turns
        }
        while (turns < 12) { // conditions for a round to continue
            System.out.println("Turn " + (turns + 1) + " starts.");
            if (numberOfActivePLayers() < 3) {
                System.out.println(numberOfActivePLayers());
                System.out.println("Less than three players remain, this turn ends.");
                break;
            }
            oneTurn();
            turns++;
        } // after the turn ends
        scoreCalculation();

        for (int i = 0; i < players.length; i++) { // printing the points
            if (!(players[i].isHuman())) {
                System.out.print("AI ");
            }
            System.out.println(players[i].getName() + " has " + players[i].getScore() + " points.");
            players[i].resetForNewRound(); // resetting players for next round
        }
        passDeckAround(); // passing the deck of cards
    }
    // method to calculate scores of each player and store them
    private void scoreCalculation() {
        for (int i = 0; i < players.length; i++) { // scoring system
            if (players[i].getLifePreservers() > 0) {  // scoring system here At the end of the round, you are awarded one point for each remaining face up life preserver.
                players[i].setScore(players[i].getLifePreservers());
            }
            else if (players[i].getLifePreservers() == 0) { // If you have no life preservers, you get no points (0).
                players[i].setScore(0);
            }
            else if (players[i].isEliminated()) { // If you were eliminated from the round because of a lack of life preservers, you receive one negative point (–1).
                players[i].setScore(-1);
            }
            if (players[i].getTideLevel() == lowestTideLevel(players)) {  // In addition, the player who, at the end of the round, had the lowest Tide Level card showing,
                players[i].setScore(1); // receives one additional bonus point.
            }
        }
    }
    // method to return lowest tide level amongst the players
    private int lowestTideLevel(Player[] players) {
        int min = players[0].getTideLevel();

        for (int i = 1; i < players.length; i++) {
            if (min >= players[0].getTideLevel())
                min = players[0].getTideLevel();
        }
        return min;
    }
    // method to pass the deck around
    private void passDeckAround() {
        Weather[] temp = decks[0];

        for (int i = 0; i < players.length - 1; i++) { // shift the decks up
            decks[i] = decks[i + 1];
        }
        decks[players.length - 1] = temp;
    }

    private int draw() {
        int[] newArray = new int[tide.length - 1];

        int drawnCardValue = tide[0]; // draw the first card

        for (int i = 1; i < tide.length; i++) {
            newArray[i - 1] = tide[i]; // resize array
        }
        tide = newArray; // reassign reference
        return drawnCardValue;
    }


    private void oneTurn() {
        Scanner in = new Scanner(System.in);
        int biggestIndex, secondBiggestIndex;
        int[] drawnTideCards = new int[2];
        Weather[] drawnWeatherCards = new Weather[players.length];

        drawnTideCards[0] = draw();
        drawnTideCards[1] = draw(); // drawing the two cards to compare later

        if (drawnTideCards[0] < drawnTideCards[1]) { // keeping the bigger tide card in the first index
            int temp = drawnTideCards[0];
            drawnTideCards[0] = drawnTideCards[1];
            drawnTideCards[1] = temp;
        }

        System.out.println("The higher tide is " + drawnTideCards[0] + " and the lower tide is " + drawnTideCards[1]); // printing prompts for the turn

        for (int i = 0; i < players.length; i++) {
            if (!(players[i].isHuman())) {
                System.out.print("AI ");
            }
            System.out.println(players[i].getName() + " has tide level " + players[i].getTideLevel()
                    + ", " + players[i].getLifePreservers() + " life preservers");
        }
        players[0].printHand();// the human players hand is printed showing options to play

        int chosenIndex = -1;

        if (!players[0].isEliminated()) {
            while (chosenIndex < 0 || chosenIndex > 12) { // taking user inputs
                try {
                    System.out.print("\nPlayer " + players[0].getName() + ", you have " + players[0].getLifePreservers()
                            + " life preservers, please select a card to play:");
                    chosenIndex = in.nextInt();
                } catch (Exception e) {
                    System.out.println(e);
                    System.exit(0);
                }

            }

            drawnWeatherCards[0] = players[0].playCard(chosenIndex); // loading player's choice
        } else {
            drawnWeatherCards[0] = null;
        }


        for (int i = 1; i < players.length; i++) {
            if (!players[i].isEliminated()) {
                drawnWeatherCards[i] = players[i].playRandomCard(); // loading the AI choices
            } else {
                drawnWeatherCards[i] = null;
            }
        }


        biggestIndex = findIndexWithBiggestCard(drawnWeatherCards);
        secondBiggestIndex = findIndexWithSecondBiggestCard(drawnWeatherCards);

        players[biggestIndex].setTideLevel(drawnTideCards[1]); // biggest weather card gets lower tide card
        players[secondBiggestIndex].setTideLevel(drawnTideCards[0]); // second-biggest weather card gets higher tide card

        drownPlayer();
    }

    private int findIndexWithBiggestCard(Weather[] cards) {

        Weather maxCard = new Weather(0);
        int maxIndex = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null && maxCard.getValue() <= cards[i].getValue()) {
                maxCard = cards[i];
                maxIndex = i; // index at the maximum card
            }
        }
        return maxIndex;
    }


    private int findIndexWithSecondBiggestCard(Weather[] cards) {
        int maxIndex = findIndexWithBiggestCard(cards);
        Weather[] newArray = new Weather[cards.length - 1]; // make a new array without the biggest card

        for (int i = 0; i < maxIndex; i++) {
            newArray[i] = cards[i];
        }
        for (int i = maxIndex + 1; i < cards.length; i++) {
            newArray[i - 1] = cards[i];
        }
        int secondMaxIndex = findIndexWithBiggestCard(newArray); // recall the previous function on the new array
        Weather c = newArray[secondMaxIndex]; // get the card from the new array

        for (int i = 0; i < cards.length; i++) { // find the index of the second-biggest card in the original array
            if (cards[i].equals(c)) {
                return i;
            }
        }
        return 0;
    }


    private void drownPlayer() {
        int maxIndex = 0;
        int maxTideLevel = players[0].getTideLevel();

        for (int i = 1; i < players.length; i++) {
            if (!(players[i].isEliminated())) {
                if (players[i].getTideLevel() > maxTideLevel) {
                    maxTideLevel = players[i].getTideLevel();
                    maxIndex = i;
                }
            } else {
                for (int j = 0; j < players[i].getCardCount(); j++) {
                    players[i].addCard(null);
                } // so that the eliminated players cannot play cards
                System.out.println("Player " + players[i].getName() + " is eliminated.");
            }
        }
        players[maxIndex].decreaseLifePreservers();
    }
}