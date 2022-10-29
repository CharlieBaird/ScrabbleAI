package application.Logic;

import java.util.ArrayList;
import java.util.Stack;

public class TileBag {

    public Stack<Character> bag;

    public TileBag()
    {
        bag = new Stack<>();
        ArrayList<Character> sortedBag = new ArrayList<>();

        addToSortedBag(sortedBag, 'A', 9);
        addToSortedBag(sortedBag, 'B', 2);
        addToSortedBag(sortedBag, 'C', 2);
        addToSortedBag(sortedBag, 'D', 4);
        addToSortedBag(sortedBag, 'E', 12);
        addToSortedBag(sortedBag, 'F', 2);
        addToSortedBag(sortedBag, 'G', 3);
        addToSortedBag(sortedBag, 'H', 2);
        addToSortedBag(sortedBag, 'I', 9);
        addToSortedBag(sortedBag, 'J', 1);
        addToSortedBag(sortedBag, 'K', 1);
        addToSortedBag(sortedBag, 'L', 4);
        addToSortedBag(sortedBag, 'M', 2);
        addToSortedBag(sortedBag, 'N', 6);
        addToSortedBag(sortedBag, 'O', 8);
        addToSortedBag(sortedBag, 'P', 2);
        addToSortedBag(sortedBag, 'Q', 1);
        addToSortedBag(sortedBag, 'R', 6);
        addToSortedBag(sortedBag, 'S', 4);
        addToSortedBag(sortedBag, 'T', 6);
        addToSortedBag(sortedBag, 'U', 4);
        addToSortedBag(sortedBag, 'V', 2);
        addToSortedBag(sortedBag, 'W', 2);
        addToSortedBag(sortedBag, 'X', 1);
        addToSortedBag(sortedBag, 'Y', 2);
        addToSortedBag(sortedBag, 'Z', 1);

        while (!sortedBag.isEmpty())
        {
            int randomIndex = (int) (Math.random() * (double) sortedBag.size());
            bag.push(sortedBag.get(randomIndex));
            sortedBag.remove(randomIndex);
        }
    }

    // Get top tile of stack in bag
    public Character pull()
    {
        return bag.isEmpty() ? null : bag.pop();
    }

    // Simple utility method to help randomize beginning bag.
    private void addToSortedBag(ArrayList<Character> sortedBag, char c, int count)
    {
        for (int i = 0; i < count; i++) {
            sortedBag.add(c);
        }
    }

}