import com.sun.media.jfxmedia.events.PlayerTimeListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class TextAnalyzer extends Application {
    private static TreeMap<String, Integer> treeMapOfWords = new TreeMap<String, Integer>();

    public static Map getListOfWordFrequencyUsingURL(String sourceUrl, String startingHtmlLine, String stoppingHtmlLine, Integer topN) throws IOException {
        clearList();

        if(sourceUrl == null || startingHtmlLine == null || stoppingHtmlLine == null) {
            return null;
        };

        String lineTemp = "";
        //TreeMap<String, Integer> treeMapOfWords = new TreeMap<String, Integer>();
        int maxForTopN = 20;

        // Get url and get ready to read
        URL url = new URL(sourceUrl);
        Scanner scan = new Scanner(url.openStream());

        // Find the title
        moveAfterTitleLine(scan, startingHtmlLine);

        // Loop through each line until the end is found
        while(scan.hasNextLine() && !lineTemp.equals(stoppingHtmlLine)) {
            // Read the line and store in temp variable
            lineTemp = scan.nextLine().trim();


            // Show the text after removing special characters and HTML tags
            //System.out.println(removeSpecialCharacters((removeHtmlTags(lineTemp))));

            // Check to see if the line is blank
            if (lineTemp.length() > 0 && lineTemp != null) {

                // Loop through the list of normalized words
                addToTreeMapFromStringArray(lineToStringArray(removeSpecialCharacters((removeHtmlTags(lineTemp)))));

            }
        }

        scan.close();


        return getTopNTreeNodes(treeMapOfWords, topN, true);
    }

    public static Map getListOfWordFrequencyUsingText(String text, Integer topN)
    {
        clearList();

        if(text == null || topN == null) {
            return null;
        }

        addToTreeMapFromStringArray(lineToStringArray(removeSpecialCharacters(text)));
        return getTopNTreeNodes(treeMapOfWords, topN, true);
    }
    public static void clearList() {
        treeMapOfWords.clear();
    }



    private static Map addToTreeMapFromStringArray(String stringArray[]) {
        for(String word : stringArray){

            // Check to see if the word is blank
            if(word.length() > 0 &&  word != null) {

                // If not a new word, then add 1 to the counter for that word
                if (treeMapOfWords.containsKey(word)) {
                    treeMapOfWords.put(word, treeMapOfWords.get(word) + 1 );
                } else {
                    // New word
                    treeMapOfWords.put(word, 1);
                }
            }
        }
        return null;
    };

    public static void main(String args[]) throws IOException {

        launch(args);

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

        //System.out.println(sortedMap.toString());
        //System.out.println(top20.toString());

        return top20;
    }
    private static String[] lineToStringArray(String line) {
        String[] words = line.toLowerCase().split("\\s+");
        return words;
    }

    private static void moveAfterTitleLine(Scanner scan, String title) {
        String lineTemp;
        boolean titleFound = false;

        while(scan.hasNextLine() && !titleFound){

            lineTemp = scan.nextLine();
            titleFound = lineTemp.equals(title);
        }
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

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TextAnalyzer.class.getResource("textAnalyzer.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 800);
        stage.setScene(scene);
        stage.setTitle("Module 6 Assignment");
        stage.show();


    }
}