package application.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Player {

    // If the bag is empty, some indices of hand
    // will be null at the end of game.
    // For example, the player could only have "S" left
    // in index 6.
    public Character[] hand;

    public String name;
    public int points;
    private TileBag bag;
    private Board board;
    private ScrabblePointsComparator comp;

    public Player (String playerName, int tileCount, TileBag tileBag, Board matrix)
    {
    	name = playerName;
        hand = new Character[tileCount];
        bag = tileBag;
        board = matrix;
        comp = new ScrabblePointsComparator();
        refreshHand();
    }
    
    // Puts current letters back into the bag
    // Resets hand
    public void trashHand()
    {
    	for (int i = 0; i < hand.length; i++)
        {
            if (hand[i] != null)
            {
            	bag.bag.push(Character.valueOf(hand[i]));
            	hand[i] = null;
            }
        }
    	
    	// Since bag is a stack, need to randomize order of it
    	Collections.shuffle(bag.bag);
    	
    	refreshHand();
    }
    
    public boolean hasEmptyHand()
    {
    	for (Character c : hand)
    	{
    		if (c != null) return false;
    	}
    	
    	return true;
    }
    
    public int pointsInHand()
    {
    	int total = 0;
    	for (Character c : hand)
    	{
    		if (c != null)
    			total += comp.map.get((char) (c.charValue() + 32));
    	}
    	
    	return total;
    }
    
    // To be called at the start, or when player plays a word on the board.
    // If possible, pulls a new letter out of the bag into the user's hand.
    public void refreshHand()
    {
        for (int i = 0; i < hand.length; i++)
        {
            if (hand[i] == null)
            {
                hand[i] = bag.pull();
            }
        }
    }
    
    public ArrayList<Play> getPlays()
    {
		ArrayList<Play> plays;
    	
    	// If board is empty, simply play best word.
    	if (board.getCurrentWords().isEmpty())
    	{
    		// Get best first play
            plays = board.getFirstPlays(hand);
    	}
    	
    	else
    	{
    		// Get best overall play
            plays = board.getPlays(hand);
    	}
    	
    	return plays;
    }
    
    public Play getBestPlay()
    {
		ArrayList<Play> plays = getPlays();
    	
    	if (plays == null || plays.isEmpty()) return null;

    	return plays.get(0);
    }

    public boolean playBestPlay()
    {
    	Play bestPlay = getBestPlay();
    	
    	if (bestPlay == null) return false;

        // Play play
        play(bestPlay);

        return true;
    }

    public void play(Play play)
    {
        // Put word on board
        ArrayList<Character> usedChars =  board.playWord(play);

        // Remove used letters from player's hand
        for (char c : usedChars)
        {
            for (int i = 0; i < hand.length; i++)
            {
                // Remove first instance
                if (hand[i] != null && hand[i] == c)
                {
                    hand[i] = null;
                    break;
                }
            }
        }

        // Add points to player's total
        points += play.getPoints();

        refreshHand();

//        board.displayBoard();
//        System.out.println(play);
//        System.out.println("New hand: " + Arrays.toString(hand));
//        System.out.println(points);
    }
    
    public Character[] getHand()
    {
    	return this.hand;
    }
    
    public int getPoints()
    {
    	return this.points;
    }

}