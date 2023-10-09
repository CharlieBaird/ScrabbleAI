# ScrabbleAI
A Scrabble AI built using Java and JavaFX for GUI.

## Examples

<a href="https://imgur.com/jT6klDU"><img src="https://i.imgur.com/jT6klDU.png" title="source: imgur.com" style="max-width:500px; max-height: 500px;"/></a>

<a href="https://imgur.com/0YMzPfa"><img src="https://i.imgur.com/0YMzPfa.png" title="source: imgur.com" style="max-width:500px; max-height: 500px;"/></a>

<a href="https://imgur.com/3Xfns1v"><img src="https://i.imgur.com/3Xfns1v.png" title="source: imgur.com" style="max-width:400px; max-height: 200px;"/></a>

## Functionality
There are two separate instruction sets, based on the context of the game:

### First Move AI
Only used for the absolute first move of the game.

- Given 7 letters, finds all combinations of the given letters using nCr
- Sort letters in combination alphabetically
- Binary search for combination in (also sorted) Scrabble dictionary
- If found, word exists. Add to a list, compute points.
  - Do the same thing for anagrams
 
- Computing points:
  - Search for best column on row 7
  - Will always use the double letter tiles if length >= 4
  - Valid columns range from [7-n, 7] if n = length

- Finally, play the move with the most computed points.

### N<sup>th</sup> Move AI (Any move where there are words on the board)
- Different from first move because must play on an already existing word
- However, this also means there are more possible words that can be played
- Two Parts of this AI:
  - Single letter words
  - Word extensions

- Single letter words
  - Uses all available individual letters one at a time, combined with player’s letters, with nCr function to find all combinations

- Word extensions
  - For each word on the board, find all Scrabble words that contain that word’s letters (in order).
  - Then, for each of those discovered words, compare missing letters to player’s letters


- At this point, we now have a list of all possible words we could play. We now must compute the best possible location for these words to be played
- For all 225 tiles on the board, do the following for both horizontal & vertical orientations:
  - Skip if placement will overlay existing word or be adjacent to none
  - Put word on board. Evaluate all resulting words to see if they exist. Skip if one doesn’t
  - If here, placement is valid. Compute points, add new Play() to list

### Calculating points of a word
Steps:
1. Find all new words on board
2. For each of these new words, do the following:
  a. Only consider bonus under tile if tile is newly placed
  b. For each letter, add score to total (plus double/triple tile) for that word.
  c. Multiply by double/triple word if applicable
  d. Add this word to overarching total

## Possible improvements
- Looking ahead in the game (can the opponent play off the triple word in his next turn as a result of my move)
- Improve “Show Best Moves” GUI
- Implement a smart scoreboard, showing how each word played is scored down to the letter

## Fun facts
- The highest score I (or the bot) achieved was 541
- The most points in a single word I came across was 140, resulting from a 90 point word plus the 50 point bonus from using all 7 letters in your hand.
