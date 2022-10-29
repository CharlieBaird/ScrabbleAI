package application.Logic;

public class Word
{
    int x;
    int y;
    boolean isVertical;
    String word;
    boolean isAffected;

    public Word(int x, int y, boolean isVertical, String word) {
        this.x = x;
        this.y = y;
        this.isVertical = isVertical;
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return x == word1.x && y == word1.y && isVertical == word1.isVertical && word.equals(word1.word);
    }

    // Returns x,y of location of selected letter
    public int[] getLocationOfLetter(int index)
    {
        if (index < 0 || index >= word.length()) return null;
        return isVertical ? new int[] {x + index, y} : new int[] {x, y + index};
    }
}