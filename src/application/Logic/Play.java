package application.Logic;

import java.util.Objects;

public class Play implements Comparable<Play>
{
    private String word;
    private int xOrigin;
    private int yOrigin;
    private boolean isVertical;
    private int points;

    // Otherwise this constructor is used
    public Play(String word, int xOrigin, int yOrigin, boolean isVertical, int points) {
        this.word = word;
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.isVertical = isVertical;
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Play play = (Play) o;
        return xOrigin == play.xOrigin && yOrigin == play.yOrigin && isVertical == play.isVertical && Objects.equals(word, play.word);
    }

    @Override
    public String toString()
    {
        return this.word + " : " + points + " at (" + this.xOrigin + ", " + this.yOrigin + ")";
    }

    @Override
    public int compareTo(Play o) {
        return points - o.points;
    }

    public String getWord() {
        return word;
    }

    public int getxOrigin() {
        return xOrigin;
    }

    public int getyOrigin() {
        return yOrigin;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public int getPoints() {
        return points;
    }
}
