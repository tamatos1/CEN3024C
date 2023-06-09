import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TextAnalyzer {
    public static void main(String args[]) throws IOException {
        String startingHtmlLine = "<h1>The Raven</h1>";
        String stoppingHtmlLine = "</div><!--end chapter-->";
        String lineTemp = "";
        TreeMap<String, Integer> treeMapOfWords = new TreeMap<String, Integer>();
        int maxForTopN = 20;
        int i = 0;

        // Get url and get ready to read
        URL url = new URL("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
        Scanner scan = new Scanner(url.openStream());

        // Find the title
        lineTemp = getTitleLine(scan, startingHtmlLine);

        // Loop through each line until the end is found
        while(scan.hasNextLine() && !lineTemp.equals(stoppingHtmlLine)) {
            // Read the line and store in temp variable

            if (i !=0) {
                lineTemp = scan.nextLine().trim();
            }

            // Check to see if the line is blank
            if (lineTemp.length() > 0 && lineTemp != null) {
                addWordsToFrequencyCounter(lineTemp, treeMapOfWords);
            }
            i++;
        }

        scan.close();

        // Print out the results
        getTopNTreeNodes(treeMapOfWords, maxForTopN, true);
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValuesDescending(final Map<K, V> map) {
        Comparator<K> valueComparator = new Comparator<K>() {
                    public int compare(K k1, K k2) {
                        int compare = map.get(k2).compareTo(map.get(k1));

                        if (compare == 0) {
                            return 1;
                        }
                        else {
                            return compare;
                        }
                    }
                };

        Map<K, V> sortedByValuesDescending = new TreeMap<K, V>(valueComparator);
        sortedByValuesDescending.putAll(map);
        return sortedByValuesDescending;
    }

    public static void addWordsToFrequencyCounter(String line, TreeMap<String, Integer> treeMap) {
        // Loop through the list of normalized words
        for(String word : lineToStringArray(removeSpecialCharacters((removeHtmlTags(line))))){

            // Check to see if the word is blank
            if(word.length() > 0 &&  word != null) {

                // If not a new word, then add 1 to the counter for that word
                if (treeMap.containsKey(word)) {
                    treeMap.put(word, treeMap.get(word) + 1 );
                } else {
                    // New word
                    treeMap.put(word, 1);
                }
            }
        }
    }
    private static Map getTopNTreeNodes(TreeMap treeMap, int maxCount, boolean printToScreen) {
        Map sortedMap = sortByValuesDescending(treeMap);
        Map<String, Integer> top20 = new HashMap<String, Integer>();

        int i = 0;
        Iterator<Map.Entry<String, Integer>> iterator = sortedMap.entrySet().iterator();
        while (iterator.hasNext() && i < maxCount) {
            Map.Entry<String, Integer> pair = iterator.next();
            top20.put(pair.getKey(), pair.getValue());

            if (printToScreen) {
                System.out.println(i+1 + ") " + pair.getKey() + " - " + pair.getValue());
            }

            i++;
        }
        top20 = sortByValuesDescending(top20);
        return top20;
    }
    private static String[] lineToStringArray(String line) {
        String[] words = line.toLowerCase().split("\\s+");
        return words;
    }

    private static String getTitleLine(Scanner scan, String title) {
        String lineTemp = "";
        boolean titleFound = false;

        while(scan.hasNextLine() && !titleFound){
            lineTemp = scan.nextLine();
            titleFound = lineTemp.equals(title);
        }

        return lineTemp;
    }

    private static String removeHtmlTags(String line) {
        String lineTemp = "";
        char startSkipChar = '<';
        char stopSkipChar = '>';
        char currentChar;
        String dashSpecial = "&mdash;";
        String finalString = "";
        StringBuilder stringBuilderTemp = new StringBuilder();
        boolean skip = false;

        lineTemp = line.replaceAll(dashSpecial, " ");

        for(int i = 0; i < lineTemp.length(); i++) {
            currentChar = lineTemp.charAt(i);

            if(currentChar != startSkipChar && currentChar != stopSkipChar && !skip) {
                stringBuilderTemp.append(currentChar);
            } else {
                if (currentChar == startSkipChar) {
                    skip = true;
                } else if (currentChar == stopSkipChar) {
                    skip = false;
                }
            }
        }

        finalString = stringBuilderTemp.toString();
        return finalString;
    }

    private static String removeSpecialCharacters(String line) {
        String finalString = "";

        finalString = line.replaceAll("[^a-zA-Z0-9\\s]", "");
        return finalString;
    }

}


