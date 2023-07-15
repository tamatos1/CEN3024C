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

import java.sql.*;

/**
 * This class is set up to be able to read in text and then analyze it to
 * determine the number of unique words.
 */
public class TextAnalyzer extends Application {

    /**
     * This treemap is the list that will be used to house the unique words
     */
    private static TreeMap<String, Integer> treeMapOfWords = new TreeMap<String, Integer>();


    /**
     * This method is the starting point for the application.
     *
     * @param  args  this is the standard string array that is passed in when using the main method
     */
    public static void main(String args[]) throws IOException {
        launch(args);

    }

    /**
     * Returns the top N number of items in a map with the list of
     * frequent words. This method uses a URL where the text is found,
     * a known starting HTML line, an ending HTML line.
     * This method is public.
     *
     * @param  sourceUrl  an HTML line that indicates the start of the document
     * @param  startingHtmlLine the HTML line that indicates where to start counting words
     * @param  stoppingHtmlLine the HTML line that indicates where to start counting words
     * @param topN the integer that represents the total number of words to return
     * @return      the map with the count of unique words
     */
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

    /**
     * Returns the top N number of items in a map with the list of
     * frequent words. This method uses a text that is passed in.
     * This method is public.
     *
     * @param  text  a text that will be read to count the number of unique words
     * @param topN the integer that represents the total number of words to return
     * @return      the map with the count of unique words
     */
    public static Map getListOfWordFrequencyUsingText(String text, Integer topN) {
        clearList();

        if(text == null || topN == null) {
            return null;
        }

        String[] textArrayTemp = lineToStringArray(removeSpecialCharacters(text));
        addToDatabaseFromStringArray(textArrayTemp, topN);
        addToTreeMapFromStringArray(textArrayTemp);

        return getTopNTreeNodes(treeMapOfWords, topN, true);
    }

    /**
     * Saves the top N number of items to a database with the list of
     * frequent words. This method uses a text that is passed in.
     * This method is public.
     *
     * @param  stringArray  a list of text that will be read to count the number of unique words
     * @param topN the integer that represents the total number of words to return
     */
    public static void addToDatabaseFromStringArray(String stringArray[], Integer topN) {
        int frequency = 0;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/word_occurrences","root","Xojte3-dizsyb-guwsun");

            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM word");
            PreparedStatement preparedStatementForSelect = connection.prepareStatement("SELECT * FROM word WHERE word = ?");
            PreparedStatement preparedStatementForInsert = connection.prepareStatement("INSERT INTO word (word, frequency) VALUES (?,?)");
            PreparedStatement preparedStatementForUpdate = connection.prepareStatement("UPDATE word SET frequency = ? WHERE word = ?");

            for(String word : stringArray) {
                // Check to see if the word is blank
                frequency = 0;

                if(word.length() > 0 &&  word != null) {
                    preparedStatementForSelect.setString(1, word);
                    ResultSet resultSet = preparedStatementForSelect.executeQuery();

                    // If not a new word, then add 1 to the counter for that word
                    if (resultSet.next() == true) {

                        frequency = resultSet.getInt("frequency");
                        preparedStatementForUpdate.setInt(1, frequency + 1);
                        preparedStatementForUpdate.setString(2, word);
                        preparedStatementForUpdate.executeUpdate();
                    } else {
                        // Insert a new word
                        preparedStatementForInsert.setString(1,word);
                        preparedStatementForInsert.setInt(2,1);
                        preparedStatementForInsert.executeUpdate();

                    }
            }
        }


            // Delete all records after the top N rows
            ResultSet resultSet = statement.executeQuery("SELECT * FROM (SELECT * FROM word ORDER BY frequency DESC, word ASC LIMIT " + topN + ") as testTable ORDER BY frequency ASC, word ASC LIMIT 1");
            resultSet.next();
            int freqTest = resultSet.getInt("frequency");
            statement.executeUpdate("DELETE FROM word WHERE frequency < " + freqTest);

            connection.close();
        }catch(Exception e){ System.out.println(e);}


    }

    /**
     * This method adds all unique words in an array to the
     * treemap of words. If a word already exists in the treemap,
     * then the word is ignored.
     *
     * @param  stringArray  an array of strings
     */
    private static void addToTreeMapFromStringArray(String stringArray[]) {
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
    };

    /**
     * This method clears the map list.
     *
     * @return void
     */
    public static void clearList() {
        treeMapOfWords.clear();
    }


    /**
     * Returns the top N number of items in a map with the list of
     * frequent words. This method uses a text that is passed in.
     *
     * @param  treeMap  a list of words (treemap)
     * @param maxCount the integer that represents the total number of words to return
     * @param printToScreen a boolean that determines if the results will be printed to the console
     * @return      the map with the count of unique words
     */
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

    /**
     * This method gets a string and splits them into a list of words.
     * It uses the space character as the delimiter.
     *
     * @param  line  a text that will be used to split into a list
     * @return      a string array
     */
    private static String[] lineToStringArray(String line) {
        String[] words = line.toLowerCase().split("\\s+");
        return words;
    }

    /**
     * This method is used when using a URL to get the text to analyze.
     * It uses a text that is passed in to find where in the text to start reading.
     *
     * @param  scan  a scanner that reads in a file
     * @param title the string that will be used to find the start of the text to read
     */
    private static void moveAfterTitleLine(Scanner scan, String title) {
        String lineTemp;
        boolean titleFound = false;

        while(scan.hasNextLine() && !titleFound){

            lineTemp = scan.nextLine();
            titleFound = lineTemp.equals(title);
        }
    }

    /**
     * This method removes HTML tags from the string that is passed in.
     * A tag starts with < and ends with >.
     *
     * @param  line  a text that will have html tags removed
     * @return      the string with the HTML tags removed
     */
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

    /**
     * This method removes special characters the string that is passed in.
     *
     * @param  line  a text that will have html tags removed
     * @return      the string with the special characters removed
     */
    private static String removeSpecialCharacters(String line) {
        String finalString = "";

        finalString = line.replaceAll("[^a-zA-Z0-9\\s]", "");
        return finalString;
    }

    /**
     * This method returns a sorted map in descending order on using the value.
     *
     * @param map  an HTML line that indicates the start of the document
     * @return      the map with the sorted map passed in
     */
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


    /**
     * This method is the starting point for the UI JavaFx application.
     *
     * @param  stage  this stage that will be used for the JavaFx application
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TextAnalyzer.class.getResource("textAnalyzer.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 800);
        stage.setScene(scene);
        stage.setTitle("Module 10 Assignment");
        stage.show();


    }
}