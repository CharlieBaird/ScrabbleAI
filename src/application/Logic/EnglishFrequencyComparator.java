package application.Logic;

import java.util.*;

public class EnglishFrequencyComparator implements Comparator<Character> {

    private HashMap<Character, Integer> frequencyDict;

    public EnglishFrequencyComparator()
    {
        frequencyDict = new HashMap<Character, Integer>();
        frequencyDict.put('z', 0);
        frequencyDict.put('j', 1);
        frequencyDict.put('x', 1);
        frequencyDict.put('q', 2);
        frequencyDict.put('k', 3);
        
        frequencyDict.put('v', 4);
        frequencyDict.put('b', 5);
        frequencyDict.put('g', 6);
        frequencyDict.put('p', 6);
        frequencyDict.put('w', 7);
        
        frequencyDict.put('y', 7);
        frequencyDict.put('f', 8);
        frequencyDict.put('c', 9);
        frequencyDict.put('m', 9);
        frequencyDict.put('u', 10);
        
        frequencyDict.put('l', 11);
        frequencyDict.put('d', 12);
        frequencyDict.put('r', 13);
        frequencyDict.put('h', 14);
        frequencyDict.put('a', 15);
        
        frequencyDict.put('i', 15);
        frequencyDict.put('n', 15);
        frequencyDict.put('o', 15);
        frequencyDict.put('s', 15);
        frequencyDict.put('t', 16);
        frequencyDict.put('e', 17);
    }

    @Override
    public int compare(Character o1, Character o2)
    {
        return frequencyDict.get(o1) - frequencyDict.get(o2);
    }

    // Util toCharacterArray method
    public static Character[] toCharacterArray(String s) {

        if ( s == null ) {
            return null;
        }

        int len = s.length();
        Character[] array = new Character[len];
        for (int i = 0; i < len ; i++)
        {
            /*
            Character(char) is deprecated since Java SE 9 & JDK 9
            Link: https://docs.oracle.com/javase/9/docs/api/java/lang/Character.html
            array[i] = new Character(s.charAt(i));
            */
            array[i] = s.charAt(i);
        }

        return array;
    }

    // Util convert back to String
    public static String fromCharacterArray(Character[] chars)
    {
        StringBuilder builder = new StringBuilder();
        for (Character c : chars) builder.append(c);
        return builder.toString();
    }
}
