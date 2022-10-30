package application.Logic;

import java.util.*;

public class Board {

    private Tile[][] board;
    public ArrayList<Word> currentWords;
    private WordHandler handler;

    public Board()
    {
        board = new Tile[15][15];
        currentWords = new ArrayList<>();

        // Insert default elements
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                board[i][j] = new Tile();
            }
        }

        // Insert Triple Words
        board[0][0].setBonus(Bonus.TRIPLE_WORD);
        board[0][7].setBonus(Bonus.TRIPLE_WORD);
        board[0][14].setBonus(Bonus.TRIPLE_WORD);
        board[7][0].setBonus(Bonus.TRIPLE_WORD);
        board[7][14].setBonus(Bonus.TRIPLE_WORD);
        board[14][0].setBonus(Bonus.TRIPLE_WORD);
        board[14][7].setBonus(Bonus.TRIPLE_WORD);
        board[14][14].setBonus(Bonus.TRIPLE_WORD);

        // Insert Double Words
        board[1][1].setBonus(Bonus.DOUBLE_WORD);
        board[2][2].setBonus(Bonus.DOUBLE_WORD);
        board[3][3].setBonus(Bonus.DOUBLE_WORD);
        board[4][4].setBonus(Bonus.DOUBLE_WORD);
        board[1][13].setBonus(Bonus.DOUBLE_WORD);
        board[2][12].setBonus(Bonus.DOUBLE_WORD);
        board[3][11].setBonus(Bonus.DOUBLE_WORD);
        board[4][10].setBonus(Bonus.DOUBLE_WORD);
        board[7][7].setBonus(Bonus.DOUBLE_WORD);
        board[10][4].setBonus(Bonus.DOUBLE_WORD);
        board[11][3].setBonus(Bonus.DOUBLE_WORD);
        board[12][2].setBonus(Bonus.DOUBLE_WORD);
        board[13][1].setBonus(Bonus.DOUBLE_WORD);
        board[10][10].setBonus(Bonus.DOUBLE_WORD);
        board[11][11].setBonus(Bonus.DOUBLE_WORD);
        board[12][12].setBonus(Bonus.DOUBLE_WORD);
        board[13][13].setBonus(Bonus.DOUBLE_WORD);

        // Insert Double Letters
        board[0][3].setBonus(Bonus.DOUBLE_LETTER);
        board[0][11].setBonus(Bonus.DOUBLE_LETTER);
        board[2][6].setBonus(Bonus.DOUBLE_LETTER);
        board[2][8].setBonus(Bonus.DOUBLE_LETTER);
        board[3][0].setBonus(Bonus.DOUBLE_LETTER);
        board[3][7].setBonus(Bonus.DOUBLE_LETTER);
        board[3][14].setBonus(Bonus.DOUBLE_LETTER);
        board[6][2].setBonus(Bonus.DOUBLE_LETTER);
        board[6][6].setBonus(Bonus.DOUBLE_LETTER);
        board[6][8].setBonus(Bonus.DOUBLE_LETTER);
        board[6][12].setBonus(Bonus.DOUBLE_LETTER);
        board[7][3].setBonus(Bonus.DOUBLE_LETTER);
        board[7][11].setBonus(Bonus.DOUBLE_LETTER);
        board[8][2].setBonus(Bonus.DOUBLE_LETTER);
        board[8][6].setBonus(Bonus.DOUBLE_LETTER);
        board[8][8].setBonus(Bonus.DOUBLE_LETTER);
        board[8][12].setBonus(Bonus.DOUBLE_LETTER);
        board[11][0].setBonus(Bonus.DOUBLE_LETTER);
        board[11][7].setBonus(Bonus.DOUBLE_LETTER);
        board[11][14].setBonus(Bonus.DOUBLE_LETTER);
        board[12][6].setBonus(Bonus.DOUBLE_LETTER);
        board[12][8].setBonus(Bonus.DOUBLE_LETTER);
        board[14][3].setBonus(Bonus.DOUBLE_LETTER);
        board[14][11].setBonus(Bonus.DOUBLE_LETTER);

        // Insert Triple Letters
        board[1][5].setBonus(Bonus.TRIPLE_LETTER);
        board[1][9].setBonus(Bonus.TRIPLE_LETTER);
        board[5][1].setBonus(Bonus.TRIPLE_LETTER);
        board[5][5].setBonus(Bonus.TRIPLE_LETTER);
        board[5][9].setBonus(Bonus.TRIPLE_LETTER);
        board[5][13].setBonus(Bonus.TRIPLE_LETTER);
        board[9][1].setBonus(Bonus.TRIPLE_LETTER);
        board[9][5].setBonus(Bonus.TRIPLE_LETTER);
        board[9][9].setBonus(Bonus.TRIPLE_LETTER);
        board[9][13].setBonus(Bonus.TRIPLE_LETTER);
        board[13][5].setBonus(Bonus.TRIPLE_LETTER);
        board[13][9].setBonus(Bonus.TRIPLE_LETTER);
    }

    public Tile[][] getBoard() {
        return board;
    }
    
    public ArrayList<Word> getCurrentWords()
    {
    	return this.currentWords;
    }
    
    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    // Puts word on board, returns used characters
    public ArrayList<Character> playWord(Play play)
    {
        ArrayList<Character> usedChars = new ArrayList<>();

        if (!play.isVertical())
        {
            for (int i = 0; i < play.getWord().length(); i++)
            {
                int x = play.getxOrigin();
                int y = play.getyOrigin() + i;
                if (board[x][y].getValue() != play.getWord().charAt(i))
                {
                    board[x][y].setValue(play.getWord().charAt(i));
                    usedChars.add(play.getWord().charAt(i));
                }

            }
        }

        else
        {
            for (int i = 0; i < play.getWord().length(); i++)
            {
                int x = play.getxOrigin() + i;
                int y = play.getyOrigin();
                if (board[x][y].getValue() != play.getWord().charAt(i))
                {
                    board[x][y].setValue(play.getWord().charAt(i));
                    usedChars.add(play.getWord().charAt(i));
                }
            }
        }

        currentWords = getAllWordsOnBoard(board, false);

        return usedChars;
    }

    public ArrayList<Word> getAllWordsOnBoard(Tile[][] matrix, boolean checkWords)
    {
        ArrayList<Word> allWords = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                char initValue = matrix[i][j].getValue();
                if (initValue != '_') {
                    // Check if start of horizontal word
                    if (j == 0 || matrix[i][j - 1].getValue() == '_') {
                        // Start of horizontal word

                        // Quickly check character to the right for a 1 letterer
                        if (!(j != 14 && matrix[i][j + 1].getValue() == '_')) {
                            StringBuilder builtWord = new StringBuilder();
                            for (int k = j; k < 15; k++) {
                                char value = matrix[i][k].getValue();
                                if (value != '_') {
                                    builtWord.append(value);
                                } else {
                                    break;
                                }
                            }

                            String built = builtWord.toString();
                            if (built.length() > 1) {
                                if (checkWords && Collections.binarySearch(handler.defaultDictionary, built) < 0)
                                {
                                    return null;
                                }

                                allWords.add(new Word(i, j, false, built));
                            }
                        }
                    }

                    if (i == 0 || matrix[i - 1][j].getValue() == '_') {
                        // Start of a vertical word

                        // Quickly check character below for a 1 letterer
                        if (!(i != 14 && matrix[i + 1][j].getValue() == '_')) {
                            StringBuilder builtWord = new StringBuilder();
                            for (int k = i; k < 15; k++) {
                                char value = matrix[k][j].getValue();
                                if (value != '_') {
                                    builtWord.append(value);
                                } else {
                                    break;
                                }
                            }

                            String built = builtWord.toString();
                            if (built.length() > 1) {
                                if (checkWords && Collections.binarySearch(handler.defaultDictionary, built) < 0)
                                {
                                    return null;
                                }

                                allWords.add(new Word(i, j, true, built));
                            }
                        }
                    }
                }
            }
        }

        return allWords;
    }

    public void displayBonuses()
    {
        for (Tile[] tiles : board)
        {
            for (Tile tile : tiles)
            {
                System.out.print(tile.getBonus().toString() + "  ");
            }
            System.out.println();
        }
    }

    public void displayBoard()
    {
        displayBoard(board);
    }

    private void displayBoard(Tile[][] matrix)
    {
        System.out.println();
        System.out.println();

        for (Tile[] tiles : matrix)
        {
            for (Tile tile : tiles)
            {
                System.out.print(tile.getValue() + "  ");
            }
            System.out.println();
        }
    }
    
    public ArrayList<Play> getFirstPlays(Character[] chars)
    {
        StringBuilder builder = new StringBuilder();
        for (Character c : chars)
        {
            if (c != null)
                builder.append(c);
        }
        String letters = builder.toString();

        // Complete brute force.
        handler = new WordHandler();

        ArrayList<Possibility> possibilities = handler.getAllPossibilities(letters, currentWords);

        return parseFirstPlays(possibilities);
    }
    
    // Returns all possible first plays that contain the tile (7,7)
    private ArrayList<Play> parseFirstPlays(ArrayList<Possibility> possibilities)
    {
    	ArrayList<Play> allPlays = new ArrayList<>();
    	
    	// All requiredPhrase components of possibilities are null.
    	for (Possibility p : possibilities)
    	{
    		String word = p.word;
    		
    		int row = 7;
    		// Min : Column 8-length so (7,7) is contained
    		// Max : Column 7 so (7,7) is contained in the word
    		for (int col = 8 - word.length(); col < 8; col++)
    		{
    			// Compute points
    			ArrayList<Word> newWords = new ArrayList<>();
    			newWords.add(new Word(row, col, false, word));
    			int points = computePoints(board, newWords);
    			
    			Play play = new Play(word, row, col, false, points);
    			allPlays.add(play);
    		}
    	}
    	
    	// Get rid of duplicates.
        Set<Play> set = new TreeSet<>(allPlays);
        ArrayList<Play> plays = new ArrayList<>(set);
        Collections.reverse(plays);
    	
    	return plays;
    }

    public ArrayList<Play> getPlays(Character[] chars)
    {
        StringBuilder builder = new StringBuilder();
        for (Character c : chars)
        {
            if (c != null)
                builder.append(c);
        }
        String letters = builder.toString();

        // Complete brute force.
        handler = new WordHandler();

        ArrayList<Possibility> possibilities = handler.getAllPossibilities(letters, currentWords);

        ArrayList<Play> allPlays = new ArrayList<>();
        for (Possibility possibility : possibilities) {
            Play play = getPlay(letters, possibility);
            if (play != null) allPlays.add(play);
        }

        // Get rid of dupes.
        Set<Play> set = new TreeSet<>(allPlays);
        ArrayList<Play> plays = new ArrayList<>(set);
        Collections.reverse(plays);

        return plays;
    }

    private Play getPlay(String letters, Possibility word)
    {
        ArrayList<Play> plays = new ArrayList<>();
        int len = word.word.length();

        for (int x = 0; x < 15; x++)
        {
            for (int y = 0; y < 15; y++)
            {
                // if z == 0, horizontal. otherwise vertical
                for (int z = 0; z < 2; z++) {
                    // Check if word would go off right edge
                    if (z == 0 && y + len >= 16) continue;

                    // Check if word would go off bottom edge
                    if (z == 1 && x + len >= 16) continue;

                    // Check neighboring tiles to see if all empty
                    if (neighboringTilesEmpty(x, y, len, z == 1)) continue;

                    // Check if word overlays another word incorrectly
                    if (wordOverlaysAnotherWord(word.word, x, y, z == 1)) continue;

                    // Finally, evaluate the board after the word is placed.
                    // Points is 0 if the word is invalid
                    int points = wordFits(word, letters, x, y, z == 1);
                    if (points == 0) continue;

                    // Possible play with this word, add it to the list
                    Play play = new Play(word.word, x, y, z == 1, points);
                    plays.add(play);
                }
            }
        }

        // If this is empty, the word cannot be placed onto the matrix
        if (plays.isEmpty()) return null;

        // Select the best play out of the discovered ones
        Play bestPlay = plays.get(0);
        for (Play play : plays)
        {
            if (play.compareTo(bestPlay) > 0)
            {
                bestPlay = play;
            }
        }

        return bestPlay;
    }

    // Check if neighboring tiles are empty. If so, there is no play.
    public boolean neighboringTilesEmpty(int x, int y, int len, boolean isVertical)
    {
        boolean checkedExactLoc = false;

        // If horizontal, check above and below
        if (!isVertical)
        {
            // Check above
            if (x != 0)
            {
                checkedExactLoc = true;
                int xLoc = x-1;
                for (int i = 0; i < len; i++) {
                    int yLoc = y+i;
                    if (board[xLoc][yLoc].getValue() != '_') return false;

                    // Also check current tile. Return false if overlays a word
                    if (board[x][yLoc].getValue() != '_') return false;
                }
            }

            // Check below
            if (x != 14)
            {
                int xLoc = x+1;
                for (int i = 0; i < len; i++) {
                    int yLoc = y+i;
                    if (board[xLoc][yLoc].getValue() != '_') return false;

                    // Prevents a double check
                    if (!checkedExactLoc)
                    {
                        // Also check current tile. Return false if overlays a word
                        if (board[x][yLoc].getValue() != '_') return false;
                    }
                }
            }
        }

        // Otherwise check left and right
        else
        {
            // Check left
            if (y != 0)
            {
                checkedExactLoc = true;
                int yLoc = y-1;
                for (int i = 0; i < len; i++) {
                    int xLoc = x+i;
                    if (board[xLoc][yLoc].getValue() != '_') return false;

                    // Also check current tile. Return false if overlays a word
                    if (board[xLoc][y].getValue() != '_') return false;
                }
            }

            // Check right
            if (y != 14)
            {
                int yLoc = y+1;
                for (int i = 0; i < len; i++) {
                    int xLoc = x+i;
                    if (board[xLoc][yLoc].getValue() != '_') return false;

                    // Prevents a double check
                    if (!checkedExactLoc)
                    {
                        // Also check current tile. Return false if overlays a word
                        if (board[xLoc][y].getValue() != '_') return false;
                    }
                }
            }
        }

        return true;
    }

    // Check if word incorrectly overlays another word
    public boolean wordOverlaysAnotherWord(String word, int x, int y, boolean isVertical)
    {
        char[] chars = word.toCharArray();

        if (!isVertical)
        {
            for (int i = 0; i < chars.length; i++)
            {
                int yLoc = y+i;
                if (board[x][yLoc].getValue() == '_')
                    continue;

                if (board[x][yLoc].getValue() != chars[i])
                    return true;
            }
        }

        else
        {
            for (int i = 0; i < chars.length; i++)
            {
                int xLoc = x+i;
                if (board[xLoc][y].getValue() == '_')
                    continue;

                if (board[xLoc][y].getValue() != chars[i])
                    return true;
            }
        }

        return false;
    }

    // Check if word fits when placed on the board
    public int wordFits(Possibility possibility, String letters, int x, int y, boolean isVertical)
    {
        String word = possibility.word;

        // Duplicate board to test word on it
        Tile[][] boardDupe = new Tile[15][15];
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                Tile baseTile = board[i][j];
                boardDupe[i][j] = new Tile(baseTile.getValue(), baseTile.getBonus());
            }
        }

        // Throw new word on board
        ArrayList<Character> changedChars = new ArrayList<>();
        if (!isVertical)
        {
            for (int i = 0; i < word.length(); i++)
            {
                char c = word.charAt(i);
                int yLoc = y+i;
                boardDupe[x][yLoc].setValue(c);

                if (board[x][yLoc].getValue() != c)
                {
                    changedChars.add(c);
                }
            }
        }

        else
        {
            for (int i = 0; i < word.length(); i++)
            {
                char c = word.charAt(i);
                int xLoc = x+i;
                boardDupe[xLoc][y].setValue(c);

                if (board[xLoc][y].getValue() != c)
                {
                    changedChars.add(c);
                }
            }
        }

        // Check if user has all the chars in changedChars to play. If not, play is invalid
        Character[] availableLettersArr = EnglishFrequencyComparator.toCharacterArray(letters);
        ArrayList<Character> availableLetters = new ArrayList<>(Arrays.asList(availableLettersArr));
        for (Character c : changedChars)
        {
            if (!availableLetters.remove(c))
            {
                return 0;
            }
        }

        // Get all words on the board. If null, one of the new words in invalid.
        ArrayList<Word> allNewWords = getAllWordsOnBoard(boardDupe, true);
        if (allNewWords == null) return 0;

        ArrayList<Word> newWords = getNewWords(allNewWords);

        return computePoints(board, newWords);
    }

    // Compare new words to currentWords list
    public ArrayList<Word> getNewWords(ArrayList<Word> allNewWords)
    {
        ArrayList<Word> newWords = new ArrayList<>();

        for (Word word : allNewWords)
        {
            boolean exists = false;
            for (Word existingWord : currentWords)
            {
                if (word.equals(existingWord))
                {
                    exists = true;
                    break;
                }
            }

            if (!exists)
            {
                newWords.add(word);
            }
        }

        return newWords;
    }

    // Compute points of a given (already checked) word on a given board
    public int computePoints(Tile[][] matrix, ArrayList<Word> newWords)
    {
        int total = 0;

        for (Word word : newWords)
        {
            // For each word, have to add something to the total
            // Only count bonus tiles for letters that were placed recently

            int wordTotal = 0;

            boolean isDoubleWord = false;
            boolean isTripleWord = false;

            for (int i = 0; i < word.word.length(); i++)
            {
                int[] location = word.getLocationOfLetter(i);
                char c = word.word.charAt(i);

                boolean checkForBonuses = tileChanged(matrix, location, c);

                int pointsValue = handler.letterPointsMap.get(c);

                if (checkForBonuses)
                {
                    switch (board[location[0]][location[1]].getBonus())
                    {
                        case NONE:
                        	wordTotal += pointsValue;
                        	break;
                        case DOUBLE_WORD:
                            wordTotal += pointsValue;
                            isDoubleWord = true;
                            break;
                        case TRIPLE_WORD:
                            wordTotal += pointsValue;
                            isTripleWord = true;
                            break;
                        case DOUBLE_LETTER:
                        	wordTotal += pointsValue * 2;
                        	break;
                        case TRIPLE_LETTER:
                        	wordTotal += pointsValue * 3;
                        	break;
                    }
                }
                else
                {
                    wordTotal += pointsValue;
                }
            }

            if (isDoubleWord) wordTotal *= 2;
            if (isTripleWord) wordTotal *= 3;

            total += wordTotal;
        }

        return total;
    }

    private boolean tileChanged(Tile[][] tiles, int[] location, char c)
    {
        char originalValue = tiles[location[0]][location[1]].getValue();
        return originalValue != c;
    }
}