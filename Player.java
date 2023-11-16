import java.util.concurrent.ThreadLocalRandom;

public class Player {
    /**
     * The number of AI players created so far.
     */
    private static int aiCount = 0;
    /**
     * The number of cards in a player's hand.
     */
    private static final int HAND_SIZE = 12;
    /**
     * The cards in the player's hand.
     */
    private Weather[] cards = new Weather[HAND_SIZE];
    /**
     * The number of life preservers the player has remaining.
     */
    private int remainingLifePreservers;
    /**
     * Whether the player has been eliminated from that round.
     */
    private boolean eliminated;
    /**
     * The tide level of the player.
     */
    private int tideLevel;
    /**
     * The player's game score (accumminated from round 1).
     */
    private int score;
    /**
     * Whether the player is human or AI.
     */
    private boolean isHuman;
    /**
     * The name of the player.
     */
    private String name;
    /**
     * Returns the name of the player.
     * 
     * @return the name of the player.
     */
    public String getName() {
        //TODO
        return name;
    }
    /**
     * Returns the tide level of the player.
     * 
     * @return the tide level of the player.
     */
    public int getTideLevel() {
        //TODO
        return tideLevel;
    }
    /**
     * Constructor Creates a new player with the given name.
     * 
     * @param name the name of the player.
     */
    public Player(String name) {
        //TODO
        this.name = name;
        isHuman = true;
        eliminated = false;
    }
    /**
     * Creates a new AI player with a default name.
     * The name of AI is "AI 1", "AI 2", "AI 3", etc.
     * Their name does not repeat.
     */
    public Player() {
        //TODO
        name = "" + (aiCount + 1);
        isHuman = false;
        eliminated = false;
        aiCount++; // increase the count of the AI
    }
    /**
     * Returns whether the player is human or AI.
     * 
     * @return whether the player is human or AI.
     */
    public boolean isHuman() {
        //TODO
        return isHuman;
    }
    /**
     * Resets the player for a new round.
     * In each new round they should have reset their attributes except
     * their name and score.
     */
    public void resetForNewRound() {
        //TODO
        remainingLifePreservers = 0;
        tideLevel = 0;
        eliminated = false; // name and score kept retained
        for (int i = 0; i < cards.length; i++) { // emptying the player's hand
            cards[i] = null;
        }
    }
    /**
     * Sets the tide level of the player. It will be called if the player
     * needs to take a tide card in a turn.
     * 
     * @param tideLevel the tide level of the player.
     */
    public void setTideLevel(int tideLevel) {
        //TODO
        this.tideLevel = tideLevel;
    }
    /**
     * This method is to deal a weather card to the player
     * 
     * @param c the card to be added.
     */
    public void addCard(Weather c) {
        //TODO
        int indexToAddCard = getCardCount();
        cards[indexToAddCard] = c;
    }

    /**
     * This method is to play a card from the player's hand.
     * 
     * @param index the index of the card to be played.
     * @return the card to be played.
     */
    public Weather playCard(int index) {
        //TODO
        Weather cardPlayed = cards[index];
        removeCard(index);
        if (isEliminated())
            return null;
        return cardPlayed;
    }
    // removing the card that has already been played
    private void removeCard(int index) {
        Weather[] newArray = new Weather[cards.length];

        for (int i = 0; i < index; i++) {
            newArray[i] = cards[i];
        }
        for (int i = index; i < cards.length - 1; i++) {
            newArray[i] = cards[i + 1];
        }
        cards = newArray;
    }
    /**
     * Returns the number of cards in the player's hand.
     * 
     * @return the number of cards in the player's hand.
     */
    public int getCardCount() {
        //TODO
        int count = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null) { // if the entries are not null then the count is incremented
                count++;
            }
        }
        return count;
    }
    /**
     * Returns a random card from the player's hand.
     * 
     * @return the card to be played.
     */
    public Weather playRandomCard() {
        //TODO
        if (getCardCount() > 0) {
            int randomIndexToPlay = ThreadLocalRandom.current().nextInt(0, getCardCount());
            return playCard(randomIndexToPlay);
        }
        return playCard(0);
    }
    /**
     * To compute the initialize life preservers for the player.
     */
    public void calcLifePreservers() {
        //TODO
        // sum up the lifepreservers from the array then round down feom 0.5 and store
        int sum = 0;

        for (int i = 0; i < getCardCount(); i++) {
            sum += cards[i].getLifePreserver(); // returns 0 1 2
        }
        if (sum/2 == 0) {
            remainingLifePreservers = 0;
        }
        else if (sum/2 < 0 || (sum - 1)/2 == 0) {
            eliminated = true;
        }
        else {
            remainingLifePreservers = sum / 2; // if even then halve the value
        }
    }
    /**
     * Returns the number of life preservers the player has remaining.
     */
    public int getLifePreservers() {
        //TODO
        return remainingLifePreservers;
    }
    /**
     * Decreases the number of life preservers the player has remaining.
     */
    public void decreaseLifePreservers() {
        //TODO
        if (remainingLifePreservers > 0) {
            remainingLifePreservers -= 1;
        }
        else {
            eliminated = true;
        }
    }
    /**
     * Returns whether the player has been eliminated from that round.
     */
    public boolean isEliminated() {
        //TODO
        return eliminated;
    }
    /**
     * Returns the player's game score (accumminated from round 1).
     */
    public int getScore() {
        //TODO
        return score;
    }
    /**
     * Set the player's game score (accumminated from round 1).
     */
    public void setScore(int score) {
        //TODO
        this.score += score;
    }
    /**
     * Prints the player's hand.
     * Please refer to the sample program for the proper format.
     * The order of the card will not affect the correctness of the program.
     * 
     * It prints nothing if the player is AI.
     */
    public void printHand() {
        //TODO
        if (isHuman) {
            for (int i = 0; i < cards.length; i++) {
                if (!(cards[i] == null)) {
                    System.out.print(cards[i] + " ");
                } else {
                    System.out.print("");
                }
            }
        }
        else
            System.out.print("");
    }
}