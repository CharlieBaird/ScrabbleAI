package application.Logic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordHandler
{
    public ArrayList<String> dictionary;
    public ArrayList<String> sortedCharDictionary;
    public ArrayList<String> defaultDictionary;
    public HashMap<Character, Integer> letterPointsMap;

    public WordHandler()
    {
        dictionary = new ArrayList<>();

        // Load dictionary
        String basePath = new File("./").getAbsolutePath();
        Path dictionaryPath = Paths.get(basePath, "resources", "unscrambledSortedDictionary.txt");

        try (BufferedReader bufferedReader = Files.newBufferedReader(dictionaryPath, StandardCharsets.UTF_8))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                dictionary.add(line);
            }
        }
        catch (IOException ex)
        {
            System.out.format("I/O error: %s%n", ex);
        }

        sortedCharDictionary = new ArrayList<>();

        // Load dictionary
        basePath = new File("./").getAbsolutePath();
        dictionaryPath = Paths.get(basePath, "resources", "scrambledSortedDictionary.txt");

        try (BufferedReader bufferedReader = Files.newBufferedReader(dictionaryPath, StandardCharsets.UTF_8))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                sortedCharDictionary.add(line);
            }
        }
        catch (IOException ex)
        {
            System.out.format("I/O error: %s%n", ex);
        }

        defaultDictionary = new ArrayList<>();

        // Load dictionary
        basePath = new File("./").getAbsolutePath();
        dictionaryPath = Paths.get(basePath, "resources", "dictionary.txt");

        try (BufferedReader bufferedReader = Files.newBufferedReader(dictionaryPath, StandardCharsets.UTF_8))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                defaultDictionary.add(line);
            }
        }
        catch (IOException ex)
        {
            System.out.format("I/O error: %s%n", ex);
        }

        letterPointsMap = new HashMap<>();

        letterPointsMap.put('A', 1);
        letterPointsMap.put('B', 3);
        letterPointsMap.put('C', 3);
        letterPointsMap.put('D', 2);
        letterPointsMap.put('E', 1);

        letterPointsMap.put('F', 4);
        letterPointsMap.put('G', 2);
        letterPointsMap.put('H', 4);
        letterPointsMap.put('I', 1);
        letterPointsMap.put('J', 8);

        letterPointsMap.put('K', 5);
        letterPointsMap.put('L', 1);
        letterPointsMap.put('M', 3);
        letterPointsMap.put('N', 1);
        letterPointsMap.put('O', 1);

        letterPointsMap.put('P', 3);
        letterPointsMap.put('Q', 10);
        letterPointsMap.put('R', 1);
        letterPointsMap.put('S', 1);
        letterPointsMap.put('T', 1);

        letterPointsMap.put('U', 3);
        letterPointsMap.put('V', 4);
        letterPointsMap.put('W', 4);
        letterPointsMap.put('X', 8);
        letterPointsMap.put('Y', 4);
        letterPointsMap.put('Z', 10);
    }

    public ArrayList<Possibility> getAllPossibilities(String input, ArrayList<Word> currentWords)
    {
        // Get all available letters on the board
        ArrayList<Character> availableChars = new ArrayList<>();
        for (int i = 0; i < currentWords.size(); i++)
        {
            Word word = currentWords.get(i);
            for (int j = 0; j < word.word.length(); j++)
            {
                char c = word.word.charAt(j);
                if (c != '_' && !availableChars.contains(c))
                {
                    availableChars.add(c);
                }
            }
        }

        ArrayList<Possibility> possibilities = new ArrayList<>();

        // Get all words able to be fully constructed from current letters (worry about placement later).
        ArrayList<String> combos = getArrangements(input);
        for (String combo : combos)
        {
            possibilities.add(new Possibility(combo));
        }

        // Get all words able to be made with a substitution, represented by _
        // First run through with available characters
        ArrayList<Possibility> comboWithChars = getArrangementsWithChars(input, availableChars);
        possibilities.addAll(comboWithChars);

        // Sort each possibility (without word thing) by english frequency order
        for (int i = 0; i < possibilities.size(); i++)
        {
            Possibility p = possibilities.get(i);
            String string = p.word;

            // Convert input string to Character[]
            Character[] chars = EnglishFrequencyComparator.toCharacterArray(string.trim().toLowerCase());

            // Sort Character[] by least common to most common letters
            Arrays.sort(chars, new EnglishFrequencyComparator());

            // Convert Character[] back to string
            p.word = EnglishFrequencyComparator.fromCharacterArray(chars);
        }

        // Check validity of these possibilities
        ArrayList<Possibility> validPossibilities = new ArrayList<>();

        for (Possibility p : possibilities)
        {
            int index = Collections.binarySearch(sortedCharDictionary, p.word);

            if (index >= 0)
            {
                String match = sortedCharDictionary.get(index);

                // Add original
                Possibility found = new Possibility(dictionary.get(index), p.requiredPhrase);
                validPossibilities.add(found);

                // Find anagrams above and below
                int dec = index;
                while (true)
                {
                    dec--;
                    if (dec >= 0 && match.equals(sortedCharDictionary.get(dec)))
                        validPossibilities.add(new Possibility(dictionary.get(dec), p.requiredPhrase));
                    else
                        break;
                }

                int inc = index;
                while (true)
                {
                    inc++;
                    if (inc < sortedCharDictionary.size() && match.equals(sortedCharDictionary.get(inc)))
                        validPossibilities.add(new Possibility(dictionary.get(inc), p.requiredPhrase));
                    else
                        break;
                }
            }
        }

        // Convert validWords to a Set to remove duplicates
        Set<Possibility> set = new HashSet<>(validPossibilities);

        // Convert back to ArrayList
        ArrayList<Possibility> words = new ArrayList<>(set);
        words.sort(new ScrabblePointsComparator());

        for (Word word : currentWords)
        {
            int length = word.word.length();
            for (String s : dictionary)
            {
                if (s.length() > length && s.contains(word.word))
                {
                    String subtracted = s.replace(word.word, "");

                    boolean valid = true;
                    for (int i = 0; i < subtracted.length(); i++)
                    {
                        if (!input.contains(String.valueOf(subtracted.charAt(i))))
                        {
                            valid = false;
                            break;
                        }
                    }

                    if (valid)
                    {
                        words.add(new Possibility(s, word.word));
                    }
                }
            }
        }

        for (Possibility possibility : words)
        {
//            System.out.println(possibility);
            possibility.word = possibility.word.toUpperCase();
        }

        return words;
    }

    // Returns arrayList of all combinations of given letters
    private ArrayList<String> getArrangements(String input)
    {
        ArrayList<String> combos = new ArrayList<>();

        for (int i = input.length(); i > 0; i--) {
            combos.addAll(getArrangements(input, i));
        }

        return combos;
    }

    private ArrayList<Possibility> getArrangementsWithChars(String input, ArrayList<Character> words)
    {
        ArrayList<Possibility> combos = new ArrayList<>();

        for (int i = input.length() + 1; i > 1; i--) {
            ArrayList<String> subs = getArrangements(input + "-", i);
            for (Character s : words)
            {
                for (String sub : subs)
                {
                    if (sub.contains("-"))
                    {
                        combos.add(new Possibility(sub.replace('-', s), s));
                    }
                }
            }
        }

        return combos;
    }

    private ArrayList<Possibility> getArrangementsWithWords(String input, ArrayList<Word> words)
    {
        ArrayList<Possibility> combos = new ArrayList<>();

        for (int i = input.length() + 1; i > 1; i--) {
            ArrayList<String> subs = getArrangements(input + "-", i);
            for (Word w : words)
            {
                for (String sub : subs)
                {
                    if (sub.contains("-"))
                    {
                        combos.add(new Possibility(sub, w.word));
                    }
                }
            }
        }

        return combos;
    }

    // Basically nCr, but get all outcomes as well
    private ArrayList<String> getArrangements(String input, int letters)
    {
        if (letters >= 15) return new ArrayList<>();

        char[] chars = input.toCharArray();

        ArrayList<int[]> comboIndices = generate(input.length(), letters);

        ArrayList<String> combos = new ArrayList<>();

        for (int[] indices : comboIndices)
        {
            StringBuilder builder = new StringBuilder();
            for (int index : indices) builder.append(chars[index]);
            combos.add(builder.toString());
        }

        return combos;
    }

    // Code from baeldung.com/java-combinations-algorithm
    // Get all indexes of nCr function
    public ArrayList<int[]> generate(int n, int r) {
        ArrayList<int[]> combinations = new ArrayList<>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations;
    }
}























