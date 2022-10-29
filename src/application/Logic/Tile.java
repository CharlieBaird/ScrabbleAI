package application.Logic;

public class Tile {

    private char value = '_';
    private Bonus bonus = Bonus.NONE;

    public Tile() {}

    public Tile(char value, Bonus bonus)
    {
        this.value = value;
        this.bonus = bonus;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }
}

