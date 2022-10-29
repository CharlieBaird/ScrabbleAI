package application.Logic;

public enum Bonus {

    TRIPLE_WORD("T"),
    DOUBLE_WORD("D"),
    DOUBLE_LETTER("2"),
    TRIPLE_LETTER("3"),
    NONE(" ");

    private final String representation;

    Bonus(String rep)
    {
        this.representation = rep;
    }

    @Override
    public String toString()
    {
        return this.representation;
    }
}
