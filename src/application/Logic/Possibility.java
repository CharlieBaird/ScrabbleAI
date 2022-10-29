package application.Logic;

import java.util.Objects;

public class Possibility {

    public String word;

    public String requiredPhrase;

    public Possibility(String word)
    {
        this.word = word;
        this.requiredPhrase = null;
    }

    public Possibility(String word, char requiredPhrase) {
        this.word = word;
        this.requiredPhrase = String.valueOf(requiredPhrase);
    }

    public Possibility(String word, String requiredPhrase) {
        this.word = word;
        this.requiredPhrase = requiredPhrase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Possibility that = (Possibility) o;
        return word.equals(that.word) && Objects.equals(requiredPhrase, that.requiredPhrase);
    }

    @Override
    public String toString()
    {
        return word + "\t" + requiredPhrase;
    }
}
