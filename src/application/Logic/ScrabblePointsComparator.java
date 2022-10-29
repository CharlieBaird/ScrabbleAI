package application.Logic;

import java.util.Comparator;
import java.util.HashMap;

public class ScrabblePointsComparator implements Comparator<Possibility> {

    public HashMap<Character, Integer> map;

    public ScrabblePointsComparator()
    {
        map = new HashMap<>();

        map.put('a', 1);
        map.put('b', 3);
        map.put('c', 3);
        map.put('d', 2);
        map.put('e', 1);

        map.put('f', 4);
        map.put('g', 2);
        map.put('h', 4);
        map.put('i', 1);
        map.put('j', 8);

        map.put('k', 5);
        map.put('l', 1);
        map.put('m', 3);
        map.put('n', 1);
        map.put('o', 1);

        map.put('p', 3);
        map.put('q', 10);
        map.put('r', 1);
        map.put('s', 1);
        map.put('t', 1);

        map.put('u', 3);
        map.put('v', 4);
        map.put('w', 4);
        map.put('x', 8);
        map.put('y', 4);
        map.put('z', 10);
    }

    @Override
    public int compare(Possibility o1, Possibility o2) {

        String o1Str = o1.word.toLowerCase();
        String o2Str = o2.word.toLowerCase();
        return total(o2Str) - total(o1Str);

    }

    public int total(String word)
    {
        int total = 0;
        char[] chars = word.toCharArray();

        for (Character c : chars)
        {
            total += map.get(c);
        }

        return total;
    }
}
